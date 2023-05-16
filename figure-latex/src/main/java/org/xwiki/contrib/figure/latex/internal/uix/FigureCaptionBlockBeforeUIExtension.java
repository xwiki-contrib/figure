/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.figure.latex.internal.uix;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.figure.internal.FigureLabelService;
import org.xwiki.contrib.figure.internal.TypedFigureTool;
import org.xwiki.contrib.latex.internal.LaTeXTool;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.FigureCaptionBlock;
import org.xwiki.rendering.block.GroupBlock;
import org.xwiki.rendering.block.IdBlock;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.script.ScriptContextManager;

import static java.util.stream.Collectors.toList;
import static org.xwiki.rendering.block.Block.Axes.DESCENDANT;

/**
 * Injects a caption setup before the caption in block element to define the localized caption label.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
@Named("org.xwiki.contrib.figure.latex.FigureCaptionBlock.before")
public class FigureCaptionBlockBeforeUIExtension extends AbstractFigureLatexUIExtension
{
    @Inject
    private FigureLabelService figureLabelService;

    @Inject
    private TypedFigureTool typedFigureTool;

    @Inject
    private ScriptContextManager scriptContextManager;

    @Inject
    private LaTeXTool latexTool;

    @Override
    public String getId()
    {
        return "org.xwiki.contrib.figure.latex.FigureCaptionBlock.before";
    }

    @Override
    public String getExtensionPointId()
    {
        return "org.xwiki.contrib.latex.FigureCaptionBlock.before";
    }

    @Override
    public Map<String, String> getParameters()
    {
        return Map.of("order", "1000");
    }

    @Override
    public Block execute()
    {
        Map<?, ?> latex = (Map<?, ?>) this.scriptContextManager.getCurrentScriptContext().getAttribute("latex");
        FigureCaptionBlock figureCaptionBlock = (FigureCaptionBlock) latex.get("block");

        return new GroupBlock(this.typedFigureTool.findParentFigure(figureCaptionBlock)
            .flatMap(figure -> this.typedFigureTool.getType(figure))
            .map(figureType -> {
                List<Block> blocks;
                if (figureType.isInline()) {
                    // In case of figure caption inside inline blocks, the caption will is transformed to plain text 
                    // and inserted the block parameter. Consequently, the id blocks are not exported and must be
                    // extracted out to be rendered.
                    blocks = figureCaptionBlock.getBlocks(IdBlock.class::isInstance, DESCENDANT)
                        .stream()
                        .map(idBlock -> {
                            String rawContent = String.format("\\label{%s}",
                                this.latexTool.normalizeLabel(((IdBlock) idBlock).getName()));
                            return new WordBlock(rawContent);
                        })
                        .collect(toList());
                } else {
                    String escapedLabel = this.latexTool.escape(this.figureLabelService.getLabel(figureType.getId()));
                    blocks = List.of(
                        new WordBlock(String.format("\\captionsetup{name=%s}", escapedLabel))
                    );
                }
                return blocks;
            }).orElse(List.of()));
    }
}
