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
import java.util.Set;

import org.xwiki.component.annotation.Role;
import org.xwiki.contrib.figure.FigureStyle;
import org.xwiki.contrib.figure.FigureType;

/**
 * Provides the operations to access the figures types configuration.
 *
 * @version $Id$
 * @since 15.4
 */
@Role
public interface FigureTypesConfiguration
{
    /**
     * @return the set of configured figure types
     */
    Set<FigureType> getFigureTypes();

    /**
     * @param id the id of a figure type
     * @return {@link Optional#empty()} if no type matching the id is found, the corresponding {@link FigureType}
     *     otherwise
     */
    Optional<FigureType> getFigureType(String id);

    /**
     * @param id the id of the figure type
     * @return the {@link FigureStyle} configure for the figure, or {@link FigureStyle#BLOCK} if the provided id is
     *     unknown
     */
    FigureStyle getFigureStyle(String id);
}
