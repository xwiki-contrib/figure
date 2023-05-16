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
import java.util.stream.Stream;

import javax.inject.Named;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.contrib.figure.FigureType;
import org.xwiki.test.LogLevel;
import org.xwiki.test.junit5.LogCaptureExtension;
import org.xwiki.test.junit5.mockito.ComponentTest;
import org.xwiki.test.junit5.mockito.InjectMockComponents;
import org.xwiki.test.junit5.mockito.MockComponent;

import ch.qos.logback.classic.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.xwiki.contrib.figure.FigureStyle.BLOCK;
import static org.xwiki.contrib.figure.FigureStyle.INLINE;
import static org.xwiki.contrib.figure.FigureType.AUTOMATIC;
import static org.xwiki.contrib.figure.FigureType.FIGURE;
import static org.xwiki.contrib.figure.FigureType.TABLE;

/**
 * Test of {@link DefaultFigureTypesConfiguration}.
 *
 * @version $Id$
 */
@ComponentTest
class DefaultFigureTypesConfigurationTest
{
    @InjectMockComponents
    private DefaultFigureTypesConfiguration configuration;

    @MockComponent
    @Named("xwikiproperties")
    private ConfigurationSource propetiesConfigurationSource;

    @MockComponent
    @Named(FigureTypesConfigurationSource.HINT)
    private ConfigurationSource objectConfigurationSource;

    @RegisterExtension
    LogCaptureExtension logCapture = new LogCaptureExtension(LogLevel.DEBUG);

    @BeforeEach
    void setUp()
    {
        when(this.objectConfigurationSource.isEmpty()).thenReturn(true);
    }

    public static Stream<Arguments> getFigureTypesSource()
    {
        return Stream.of(
            Arguments.of(
                List.of(),
                Set.of(AUTOMATIC, FIGURE, TABLE),
                false
            ),
            Arguments.of(
                List.of("figure", "lemma"),
                Set.of(AUTOMATIC, FIGURE, TABLE, new FigureType("lemma", BLOCK)),
                true
            ),
            Arguments.of(
                List.of("figure", "lemma"),
                Set.of(AUTOMATIC, FIGURE, TABLE, new FigureType("lemma", BLOCK)),
                true
            )
        );
    }

    @ParameterizedTest
    @MethodSource("getFigureTypesSource")
    void getFigureTypes(List<String> property, Set<FigureType> expected, boolean logDuplicatedFigureExpected)
    {
        when(this.propetiesConfigurationSource.getProperty("figure.block.types", List.class)).thenReturn(property);
        when(this.propetiesConfigurationSource.getProperty("figure.inline.types", List.class)).thenReturn(List.of());
        Set<FigureType> figureTypes = this.configuration.getFigureTypes();
        assertEquals(expected, figureTypes);

        if (logDuplicatedFigureExpected) {
            assertEquals("Duplicated figure type [figure] found.", this.logCapture.getMessage(0));
            assertEquals(Level.DEBUG, this.logCapture.getLogEvent(0).getLevel());
        }
    }

    @Test
    void getFigureType()
    {
        assertEquals(Optional.of(FIGURE), this.configuration.getFigureType("figure"));
    }

    @Test
    void getFigureTypeUnknown()
    {
        assertEquals(Optional.empty(), this.configuration.getFigureType("unknown"));
    }

    @Test
    void getFigureStyle()
    {
        assertEquals(BLOCK, this.configuration.getFigureStyle("figure"));
    }

    @Test
    void getFigureStyleUnknwon()
    {
        assertEquals(BLOCK, this.configuration.getFigureStyle("unknown"));
    }

    @Test
    void getFigureStyleInline()
    {
        when(this.propetiesConfigurationSource.getProperty("figure.inline.types", List.class)).thenReturn(List.of(
            "lemma"
        ));
        assertEquals(INLINE, this.configuration.getFigureStyle("lemma"));
    }
}
