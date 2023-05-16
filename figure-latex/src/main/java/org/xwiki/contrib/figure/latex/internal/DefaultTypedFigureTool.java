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
package org.xwiki.contrib.figure.latex.internal;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.figure.FigureType;
import org.xwiki.contrib.figure.internal.TypedFigureTool;
import org.xwiki.contrib.latex.internal.DefaultFigureTool;
import org.xwiki.rendering.block.AbstractBlock;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.FigureBlock;
import org.xwiki.rendering.block.FigureCaptionBlock;
import org.xwiki.rendering.block.GroupBlock;

import static org.xwiki.rendering.block.Block.Axes.DESCENDANT;

/**
 * Override of {@link DefaultFigureTool} with specifics introduced by the notion of figure style and custom figures.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
public class DefaultTypedFigureTool extends DefaultFigureTool
{
    @Inject
    protected TypedFigureTool typedFigureTool;

    @Override
    public String getFigureEnvironment(Block figureBlock)
    {
        Optional<FigureType> type = this.typedFigureTool.getType(figureBlock);
        String figureEnvironment;
        if (type.isPresent()) {
            figureEnvironment = type.get().getId();
        } else {
            // In case of unknown figure type, we fall back to the default figure type identification.
            figureEnvironment = super.getFigureEnvironment(figureBlock);
        }
        return figureEnvironment;
    }

    @Override
    public Block getFigureEnvironmentParameter(FigureBlock figureBlock)
    {
        if (this.typedFigureTool.isInline(figureBlock)) {
            return new GroupBlock(Optional.ofNullable(
                    figureBlock.<FigureCaptionBlock>getFirstBlock(FigureCaptionBlock.class::isInstance, DESCENDANT))
                .map(AbstractBlock::getChildren).orElse(List.of()));
        } else {
            return super.getFigureEnvironmentParameter(figureBlock);
        }
    }

    @Override
    public boolean displayFigureCaption(FigureCaptionBlock figureCaptionBlock)
    {
        return this.typedFigureTool.findParentFigure(figureCaptionBlock)
            .map(figure -> !this.typedFigureTool.isInline(figure))
            .orElse(false);
    }
}
