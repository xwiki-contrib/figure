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

import javax.inject.Inject;

import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;
import org.xwiki.contrib.latex.internal.LaTeXBlockRenderer;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.syntax.SyntaxRegistry;
import org.xwiki.uiextension.UIExtension;

/**
 * Common code for all the UIXs of Figure LaTeX. Mainly initialize the component with the "latex/1.0" syntax using the
 * {@link SyntaxRegistry}.
 *
 * @version $Id$
 * @since 15.4
 */
public abstract class AbstractFigureLatexUIExtension implements UIExtension, Initializable
{
    protected Syntax latexSyntax;

    @Inject
    private SyntaxRegistry syntaxRegistry;

    @Override
    public void initialize() throws InitializationException
    {
        try {
            this.latexSyntax = this.syntaxRegistry.resolveSyntax(LaTeXBlockRenderer.ROLEHINT);
        } catch (ParseException e) {
            throw new InitializationException(
                String.format("Unable to find the [%s] syntax on the registry.", LaTeXBlockRenderer.ROLEHINT), e);
        }
    }
}
