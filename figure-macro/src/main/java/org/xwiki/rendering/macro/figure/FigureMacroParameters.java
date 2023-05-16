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
package org.xwiki.rendering.macro.figure;

import java.util.Optional;

import org.xwiki.contrib.figure.FigureType;
import org.xwiki.contrib.figure.internal.FigureMacro;
import org.xwiki.properties.annotation.PropertyDescription;
import org.xwiki.properties.annotation.PropertyName;
import org.xwiki.stability.Unstable;

/**
 * Parameters for the {@link FigureMacro}.
 *
 * @version $Id$
 * @since 14.9RC1
 */
@Unstable
public class FigureMacroParameters
{
    /**
     * By default, the figure type is set to {@code null}, meaning that the type will be computed automatically.
     */
    private FigureType type;

    /**
     * @return the type of the figure (e.g., "figure" or "table"), if {@link Optional#empty()} the type will be
     *     automatically defined based on the macro content
     */
    public FigureType getType()
    {
        return this.type;
    }

    /**
     * @param type the type of the figure (e.g., "figure" or "table"), if {@code null} the type will be
     *     automatically defined based on the macro content
     */
    @PropertyDescription("The type of the figure (e.g., \"figure\" or \"table\"). When the type \"automatic\", the "
        + "actual type will be defined based on the macro content.")
    @PropertyName("Type")
    public void setType(FigureType type)
    {
        this.type = type;
    }
}
