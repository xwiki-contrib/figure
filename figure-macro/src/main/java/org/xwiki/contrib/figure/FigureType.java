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

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.stability.Unstable;
import org.xwiki.text.XWikiToStringBuilder;

import static org.xwiki.contrib.figure.FigureStyle.BLOCK;
import static org.xwiki.contrib.figure.FigureStyle.INLINE;

/**
 * Stores information about a figure type: its id and whether it is the automatic type. Only the {@link #AUTOMATIC} has
 * {@link #isAutomatic()} which returns {@code true}.
 *
 * @version $Id$
 * @since 15.4
 */
@Unstable
public class FigureType
{
    /**
     * A constant figure type when the type must be computed automatically based on the figure content.
     */
    public static final FigureType AUTOMATIC = new FigureType("automatic", null, true);

    /**
     * The figure type, which is always available and the selected type when automatic is activated and no table is
     * detected in the content.
     */
    public static final FigureType FIGURE = new FigureType("figure", BLOCK);

    /**
     * The table type, which is always available and the selected type when automatic is activated and a table is
     * detected in the content.
     */
    public static final FigureType TABLE = new FigureType("table", BLOCK);

    /**
     * List of default figure types.
     */
    public static final List<String> DEFAULT_FIGURE_TYPES =
        List.of(FigureType.FIGURE.getId(), FigureType.TABLE.getId(), FigureType.AUTOMATIC.getId());

    private final String id;

    private final FigureStyle figureStyle;

    /**
     * This field is only {@code true} for {@link #AUTOMATIC} as the constructor allowing to set this field to true is
     * {@code private}.
     */
    private final boolean isAutomatic;

    /**
     * Default constructor, initializes the figure type as not automatic.
     *
     * @param id the figure type id
     * @param figureStyle the style of the figure (i.e., "block", or "inline")
     */
    public FigureType(String id, FigureStyle figureStyle)
    {
        this(id, figureStyle, false);
    }

    private FigureType(String id, FigureStyle figureStyle, boolean isAutomatic)
    {
        this.id = id;
        this.figureStyle = figureStyle;
        this.isAutomatic = isAutomatic;
    }

    /**
     * @return the figure id (e.g., {@code "figure"})
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * @return {@code true} when the figure type should be computed automatically.
     */
    public boolean isAutomatic()
    {
        return this.isAutomatic;
    }

    /**
     * @return the style of the figure (i.e., "block" or "inline")
     */
    public FigureStyle getFigureStyle()
    {
        return this.figureStyle;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FigureType that = (FigureType) o;

        return new EqualsBuilder()
            .append(this.isAutomatic, that.isAutomatic)
            .append(this.figureStyle, that.figureStyle)
            .append(this.id, that.id)
            .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37)
            .append(this.id)
            .append(this.figureStyle)
            .append(this.isAutomatic)
            .toHashCode();
    }

    @Override
    public String toString()
    {
        return new XWikiToStringBuilder(this)
            .append("id", this.id)
            .append("figureStyle", this.figureStyle)
            .append("isAutomatic", this.isAutomatic)
            .toString();
    }

    /**
     * @return {@code true} when the figure is configured to be displayed inline, {@code false} when the figure is
     *     configured to be displayed as block (i.e., the default mode)
     */
    public boolean isInline()
    {
        return Objects.equals(this.figureStyle, INLINE);
    }
}
