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

import org.junit.jupiter.api.Test;
import org.xwiki.localization.ContextualLocalizationManager;
import org.xwiki.localization.Translation;
import org.xwiki.test.junit5.mockito.ComponentTest;
import org.xwiki.test.junit5.mockito.InjectMockComponents;
import org.xwiki.test.junit5.mockito.MockComponent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test of {@link DefaultFigureLabelService}.
 *
 * @version $Id$
 */
@ComponentTest
class DefaultFigureLabelServiceTest
{
    @InjectMockComponents
    private DefaultFigureLabelService figureLabelService;

    @MockComponent
    private ContextualLocalizationManager contextLocalization;

    @Test
    void getLabel()
    {
        when(this.contextLocalization.getTranslation("org.xwiki.rendering.macro.figure.FigureType.figure"))
            .thenReturn(mock(Translation.class));
        when(this.contextLocalization.getTranslationPlain("org.xwiki.rendering.macro.figure.FigureType.figure"))
            .thenReturn("Figura");
        assertEquals("Figura", this.figureLabelService.getLabel("figure"));
    }

    @Test
    void getLabelFallbackUppercase()
    {
        when(this.contextLocalization.getTranslation("org.xwiki.rendering.macro.figure.FigureType.FIGURE"))
            .thenReturn(mock(Translation.class));
        when(this.contextLocalization.getTranslationPlain("org.xwiki.rendering.macro.figure.FigureType.FIGURE"))
            .thenReturn("FigurA");
        assertEquals("FigurA", this.figureLabelService.getLabel("figure"));
    }

    @Test
    void getLabelNoTranslation()
    {
        assertEquals("Figure", this.figureLabelService.getLabel("figure"));
        verify(this.contextLocalization, never()).getTranslationPlain("org.xwiki.rendering.macro.figure.FigureType.figure");
    }
}
