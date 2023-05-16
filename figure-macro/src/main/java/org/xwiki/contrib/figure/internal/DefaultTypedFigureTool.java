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

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.figure.FigureType;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.FigureBlock;
import org.xwiki.rendering.block.FigureCaptionBlock;

import static org.xwiki.contrib.figure.internal.FigureTypeRecognizerMacro.DATA_XWIKI_RENDERING_FIGURE_TYPE;

/**
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
public class DefaultTypedFigureTool implements TypedFigureTool
{
    @Inject
    private FigureTypesConfiguration figureTypesConfiguration;

    @Override
    public boolean isInline(FigureBlock figureBlock)
    {
        return getType(figureBlock)
            .map(FigureType::isInline)
            .orElse(false);
    }

    @Override
    public Optional<FigureBlock> findParentFigure(FigureCaptionBlock figureCaptionBlock)
    {
        Block parent = figureCaptionBlock.getParent();
        while (parent != null) {
            if (parent instanceof FigureBlock) {
                return Optional.of((FigureBlock) parent);
            }
            parent = parent.getParent();
        }

        return Optional.empty();
    }

    @Override
    public Optional<FigureType> getType(Block figureBlock)
    {
        String id = figureBlock.getParameter(DATA_XWIKI_RENDERING_FIGURE_TYPE);
        return this.figureTypesConfiguration.getFigureType(id);
    }
}
