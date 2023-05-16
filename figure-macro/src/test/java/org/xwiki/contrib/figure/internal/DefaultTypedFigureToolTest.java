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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xwiki.rendering.block.FigureBlock;
import org.xwiki.rendering.block.FigureCaptionBlock;
import org.xwiki.rendering.block.GroupBlock;
import org.xwiki.test.junit5.mockito.ComponentTest;
import org.xwiki.test.junit5.mockito.InjectMockComponents;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test of {@link DefaultTypedFigureTool}.
 *
 * @version $Id$
 */
@ComponentTest
class DefaultTypedFigureToolTest
{
    @InjectMockComponents
    private DefaultTypedFigureTool defaultTypedFigureTool;

    @Test
    void findParentFigureNoFigureParent()
    {
        FigureCaptionBlock figureCaptionBlock = new FigureCaptionBlock(List.of());
        assertEquals(Optional.empty(), this.defaultTypedFigureTool.findParentFigure(figureCaptionBlock));
    }

    @Test
    void findParentFigureNoFigureParentWithContent()
    {
        FigureCaptionBlock figureCaptionBlock = new FigureCaptionBlock(List.of());
        new GroupBlock(List.of(figureCaptionBlock));
        assertEquals(Optional.empty(), this.defaultTypedFigureTool.findParentFigure(figureCaptionBlock));
    }


    @Test
    void findParentFigureDirectParent()
    {
        FigureCaptionBlock figureCaptionBlock = new FigureCaptionBlock(List.of());
        FigureBlock figureBlock = new FigureBlock(List.of(figureCaptionBlock));
        assertEquals(Optional.of(figureBlock), this.defaultTypedFigureTool.findParentFigure(figureCaptionBlock));
    }
    
    @Test
    void findParentFigure2LevelsParent()
    {
        FigureCaptionBlock figureCaptionBlock = new FigureCaptionBlock(List.of());
        FigureBlock figureBlock = new FigureBlock(List.of(new GroupBlock(List.of(figureCaptionBlock))));
        assertEquals(Optional.of(figureBlock), this.defaultTypedFigureTool.findParentFigure(figureCaptionBlock));
    }
}
