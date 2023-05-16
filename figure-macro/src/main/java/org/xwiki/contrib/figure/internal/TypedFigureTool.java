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

import org.xwiki.component.annotation.Role;
import org.xwiki.contrib.figure.FigureType;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.FigureBlock;
import org.xwiki.rendering.block.FigureCaptionBlock;
import org.xwiki.stability.Unstable;

/**
 * @version $Id$
 * @since 15.4
 */
@Unstable
@Role
public interface TypedFigureTool
{
    /**
     * @param figureBlock the figure block to analyze
     * @return {@code true} if the figure block is "inline", {@code false} if it has an "inline" style
     */
    boolean isInline(FigureBlock figureBlock);

    /**
     * @param figureCaptionBlock a figure caption block
     * @return the closed parent {@link FigureBlock}, or {@link Optional#empty()} if none found
     */
    Optional<FigureBlock> findParentFigure(FigureCaptionBlock figureCaptionBlock);

    /**
     * @param figureBlock a figure block
     * @return the type of the provided figure block, or {@link Optional#empty()} if none found
     */
    Optional<FigureType> getType(Block figureBlock);

}
