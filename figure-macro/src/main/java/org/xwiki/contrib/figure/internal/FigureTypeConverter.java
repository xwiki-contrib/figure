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

import java.lang.reflect.Type;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.figure.FigureType;
import org.xwiki.properties.converter.AbstractConverter;

import static org.xwiki.contrib.figure.FigureStyle.BLOCK;
import static org.xwiki.contrib.figure.FigureType.AUTOMATIC;

/**
 * Convert a figure type to and from a {@link String}.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
public class FigureTypeConverter extends AbstractConverter<FigureType>
{
    @Inject
    private FigureTypesConfiguration figureTypesConfiguration;

    @Override
    protected String convertToString(FigureType value)
    {
        if (value == null) {
            return null;
        }
        return value.getId();
    }

    @Override
    protected FigureType convertToType(Type targetType, Object value)
    {
        if (value == null) {
            return AUTOMATIC;
        }

        String stringValue = String.valueOf(value);
        if (Objects.equals(AUTOMATIC.getId(), stringValue.toLowerCase())) {
            return AUTOMATIC;
        } else {
            return this.figureTypesConfiguration
                .getFigureType(stringValue)
                // Fallback to a new figure type of block style if unknown
                .orElse(new FigureType(stringValue, BLOCK));
        }
    }
}
