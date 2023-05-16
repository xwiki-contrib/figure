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
package org.xwiki.contrib.figure.latex.internal.uix;

import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.RawBlock;

/**
 * Injects the {@code newfloat} package in the import section of the latex document.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
@Named("org.xwiki.contrib.figure.latex.Preamble.usepackage.after")
public class PreambleUsepackageUIExtension extends AbstractFigureLatexUIExtension
{
    @Override
    public String getId()
    {
        return "org.xwiki.contrib.figure.latex.Preamble.usepackage.after";
    }

    @Override
    public String getExtensionPointId()
    {
        return "org.xwiki.contrib.latex.Preamble.usepackage.after";
    }

    @Override
    public Map<String, String> getParameters()
    {
        return Map.of("order", "1000");
    }

    @Override
    public Block execute()
    {
        return new RawBlock("\\usepackage{newfloat}", this.latexSyntax);
    }
}
