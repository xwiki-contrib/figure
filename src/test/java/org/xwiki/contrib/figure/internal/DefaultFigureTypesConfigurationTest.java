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
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Named;

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
    private ConfigurationSource configurationSource;

    @RegisterExtension
    LogCaptureExtension logCapture = new LogCaptureExtension(LogLevel.DEBUG);

    public static Stream<Arguments> getFigureTypesSource()
    {
        return Stream.of(
            Arguments.of(
                List.of(),
                Set.of(AUTOMATIC, FIGURE, TABLE),
                true,
                false
            ),
            Arguments.of(
                List.of("figure", "lemma"),
                Set.of(AUTOMATIC, FIGURE, TABLE, new FigureType("lemma")),
                false,
                false
            ),
            Arguments.of(
                List.of("figure", "lemma", "figure"),
                Set.of(AUTOMATIC, FIGURE, TABLE, new FigureType("lemma")),
                false,
                true
            )
        );
    }

    @ParameterizedTest
    @MethodSource("getFigureTypesSource")
    void getFigureTypes(List<String> property, Set<FigureType> expected, boolean logMissingFigureExpected,
        boolean logDuplicatedFigureExpected)
    {
        when(this.configurationSource.getProperty("figure.types", List.class)).thenReturn(property);
        Set<FigureType> figureTypes = this.configuration.getFigureTypes();
        assertEquals(expected, figureTypes);
        if (logMissingFigureExpected) {
            assertEquals("Mandatory type [figure] added.", this.logCapture.getMessage(0));
            assertEquals(Level.DEBUG, this.logCapture.getLogEvent(0).getLevel());
        }
        if (logDuplicatedFigureExpected) {
            assertEquals("Duplicated figure type [figure] found.", this.logCapture.getMessage(0));
            assertEquals(Level.DEBUG, this.logCapture.getLogEvent(0).getLevel());
        }
        int tableLogIndex = logMissingFigureExpected || logDuplicatedFigureExpected ? 1 : 0;
        assertEquals("Mandatory type [table] added.", this.logCapture.getMessage(tableLogIndex));
        assertEquals(Level.DEBUG, this.logCapture.getLogEvent(tableLogIndex).getLevel());
    }
}
