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

import org.xwiki.stability.Unstable;

/**
 * The style of the figure. The style impacts the way the figure will be rendered, in pages, WYSIWYG editors, and
 * exports (e.g., LaTeX exports).
 *
 * @version $Id$
 * @since 15.4
 */
@Unstable
public enum FigureStyle
{
    /**
     * The block style.
     */
    BLOCK("block"),

    /**
     * The inline style.
     */
    INLINE("inline");

    private final String name;

    FigureStyle(String name)
    {
        this.name = name;
    }

    /**
     * @return the name of the style
     */
    public String getName()
    {
        return this.name;
    }
}
