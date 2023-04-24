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

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.figure.internal.FigureTypesConfiguration;
import org.xwiki.script.service.ScriptService;

/**
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

    /**
     * @return the set of configured figure types
     */
    public Set<FigureType> getFigureTypes()
    {
        return this.figureTypesConfiguration.getFigureTypes();
    }

    /**
     * @return a map of counters and their associated types (e.g.,
     *     {@code Map.of("figure", Set.of("figure"), "math", Set.of("proof", "lemma"))})
     */
    public Map<String, Set<FigureType>> getFigureCounters()
    {
        return this.figureTypesConfiguration.getFigureCounters();
    }

    /**
     * Resolve the counter for a given figure type.
     *
     * @param type a figure type (e.g., {@code "proof"})
     * @return the resolve counter (e.g., "math")
     */
    public String getCounter(String type)
    {
        return this.figureTypesConfiguration.getCounter(type);
    }
}
