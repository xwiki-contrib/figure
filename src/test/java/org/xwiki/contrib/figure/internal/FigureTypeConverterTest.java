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

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xwiki.contrib.figure.FigureType;
import org.xwiki.test.junit5.mockito.ComponentTest;
import org.xwiki.test.junit5.mockito.InjectMockComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.xwiki.contrib.figure.FigureType.AUTOMATIC;

/**
 * Test of {@link FigureTypeConverter}.
 *
 * @version $Id$
 * @since 15.4
 */
@ComponentTest
class FigureTypeConverterTest
{
    @InjectMockComponents
    private FigureTypeConverter converter;

    public static Stream<Arguments> convertToStringSource()
    {
        return Stream.of(
            arguments(null, null),
            arguments(AUTOMATIC, AUTOMATIC.getId()),
            arguments(new FigureType("test"), "test")
        );
    }

    @ParameterizedTest
    @MethodSource("convertToStringSource")
    void convertToString(FigureType figureType, String expected)
    {
        assertEquals(expected, this.converter.convertToString(figureType));
    }

    @Test
    void convertToType()
    {
//        this.converter.convertToType(FigureType.class)
        // TODO: test be tested
    }
}
