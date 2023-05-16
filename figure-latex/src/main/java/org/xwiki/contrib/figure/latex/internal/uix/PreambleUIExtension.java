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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.figure.internal.FigureLabelService;
import org.xwiki.contrib.figure.internal.FigureTypesConfiguration;
import org.xwiki.contrib.latex.internal.LaTeXTool;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.RawBlock;

/**
 * Injects the theorem and floating environment declarations in the preamble of the generated document.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
@Named("org.xwiki.contrib.figure.latex.Preamble.after")
public class PreambleUIExtension extends AbstractFigureLatexUIExtension
{
    @Inject
    private FigureTypesConfiguration figureTypesConfiguration;

    @Inject
    private FigureLabelService figureLabelService;

    @Inject
    private LaTeXTool latexTool;

    @Override
    public String getId()
    {
        return "org.xwiki.contrib.figure.latex.Preamble.after";
    }

    @Override
    public String getExtensionPointId()
    {
        return "org.xwiki.contrib.latex.Preamble.after";
    }

    @Override
    public Map<String, String> getParameters()
    {
        return Map.of("order", "1000");
    }

    @Override
    public Block execute()
    {
        String newLine = System.lineSeparator();
        String collect = this.figureTypesConfiguration.getFigureTypes()
            .stream()
            .filter(it -> !List.of("figure", "table").contains(it.getId()))
            .map(type -> {
                String typeDeclaration;
                String typeId = type.getId();
                if (type.isInline()) {
                    typeDeclaration =
                        String.format("\\newtheorem{%s}{%s}", this.latexTool.escape(typeId),
                            this.latexTool.escape(this.figureLabelService.getLabel(typeId)));
                } else {
                    typeDeclaration = String.format("\\DeclareFloatingEnvironment{%s}", typeId);
                }
                return typeDeclaration;
            })
            .collect(Collectors.joining(newLine));

        return new RawBlock(collect, this.latexSyntax);
    }
}
