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
package org.xwiki.contrib.figure.internal;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.FigureBlock;
import org.xwiki.rendering.block.FigureCaptionBlock;
import org.xwiki.rendering.block.MacroBlock;
import org.xwiki.rendering.block.MetaDataBlock;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.macro.AbstractNoParameterMacro;
import org.xwiki.rendering.macro.MacroContentParser;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.macro.descriptor.DefaultContentDescriptor;
import org.xwiki.rendering.transformation.MacroTransformationContext;
import org.xwiki.rendering.util.ErrorBlockGenerator;

/**
 * Provides a caption inside the Figure Macro content. Needs to be used as the first or last block.
 *
 * @version $Id$
 * @since 10.2
 */
@Component
@Named(FigureCaptionMacro.HINT)
@Singleton
public class FigureCaptionMacro extends AbstractNoParameterMacro
{
    /**
     * Figure caption macro hint.
     *
     * @since 15.4
     */
    public static final String HINT = "figureCaption";

    /**
     * The description of the macro.
     */
    private static final String DESCRIPTION = "Provide a figure caption when used inside the Figure macro.";

    /**
     * The description of the macro content.
     */
    private static final String CONTENT_DESCRIPTION = "Caption content";

    @Inject
    private MacroContentParser contentParser;

    @Inject
    private ErrorBlockGenerator errorBlockGenerator;

    @Inject
    private TypedFigureTool figureTypesConfiguration;

    /**
     * Create and initialize the descriptor of the macro.
     */
    public FigureCaptionMacro()
    {
        super("Figure Caption", DESCRIPTION,
            new DefaultContentDescriptor(CONTENT_DESCRIPTION, true, Block.LIST_BLOCK_TYPE));
        setDefaultCategories(Set.of(DEFAULT_CATEGORY_DEVELOPMENT));
    }

    @Override
    public boolean supportsInlineMode()
    {
        return false;
    }

    @Override
    public List<Block> execute(Object unusedParameters, String content, MacroTransformationContext context)
        throws MacroExecutionException
    {

        List<Block> result;

        Optional<List<Block>> error = checkLocation(context.getCurrentMacroBlock());
        if (error.isPresent()) {
            result = error.get();
        } else {
            XDOM xdom = this.contentParser.parse(content, context, false, false);
            List<Block> figureCaptionChildren = xdom.getChildren();

            // Mark the macro content as being content that has not been transformed (so that it can be edited inline).
            figureCaptionChildren = List.of(new MetaDataBlock(figureCaptionChildren, getNonGeneratedContentMetaData()));
            result = List.of(new FigureCaptionBlock(figureCaptionChildren));
        }

        return result;
    }

    private Optional<List<Block>> checkLocation(MacroBlock currentMacroBlock)
    {
        Optional<List<Block>> error = Optional.empty();

        Block parent = currentMacroBlock.getParent();
        FigureBlock figureParent = null;

        if (parent instanceof FigureBlock) {
            figureParent = (FigureBlock) parent;
        } else if (parent instanceof MetaDataBlock && parent.getParent() instanceof FigureBlock) {
            figureParent = (FigureBlock) parent.getParent();
        }

        if (figureParent != null) {
            int currentMacroIndex = parent.getChildren().indexOf(currentMacroBlock);

            boolean isInline = this.figureTypesConfiguration.isInline(figureParent);

            if (!isInline && (currentMacroIndex != 0 && currentMacroIndex != parent.getChildren().size() - 1)) {
                error = Optional.of(this.errorBlockGenerator.generateErrorBlocks(false,
                    "figure.macro.figureCaption.error.blockPositionError",
                    "The figure caption must be the first or last element of the figure.", null));
            } else if (isInline && currentMacroIndex != 0) {
                error = Optional.of(this.errorBlockGenerator.generateErrorBlocks(false,
                    "figure.macro.figureCaption.error.inlinePositionError",
                    "The caption of inline figure must be the first element of the figure.", null));
            }
        } else {
            error = Optional.of(
                this.errorBlockGenerator.generateErrorBlocks(false, "figure.macro.figureCaption.error.standaloneError",
                    "The figure caption must be located inside a figure macro.", null));
        }

        return error;
    }
}
