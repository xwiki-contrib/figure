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
package org.xwiki.contrib.figure;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.figure.internal.FigureLabelService;
import org.xwiki.contrib.figure.internal.FigureTypesConfiguration;
import org.xwiki.script.service.ScriptService;

/**
 * Gives access to the figures types, in particular the list of configured figure types as well at the localized label o
 * the figure types.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
@Named("figureTypes")
public class FigureTypesScriptService implements ScriptService
{
    @Inject
    private FigureTypesConfiguration figureTypesConfiguration;

    @Inject
    private FigureLabelService figureLabelService;

    /**
     * @return the set of configured figure types
     */
    public Set<FigureType> getFigureTypes()
    {
        return this.figureTypesConfiguration.getFigureTypes();
    }

    /**
     * @param type the figure type
     * @return the translated value if key {@code org.xwiki.rendering.macro.figure.FigureType.$type} is found, the
     *     capitalized type otherwise
     */
    public String getLabel(String type)
    {
        return this.figureLabelService.getLabel(type);
    }
}
