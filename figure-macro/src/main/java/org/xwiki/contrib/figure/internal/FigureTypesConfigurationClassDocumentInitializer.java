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

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.LocalDocumentReference;

import com.xpn.xwiki.doc.AbstractMandatoryClassInitializer;
import com.xpn.xwiki.objects.classes.BaseClass;

import static com.xpn.xwiki.objects.classes.ListClass.DISPLAYTYPE_INPUT;
import static com.xpn.xwiki.objects.classes.ListClass.FREE_TEXT_ALLOWED;

/**
 * {@code XWiki.Figure.FigureTypeClass} XClass initializer.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Named("XWiki.Figure.FigureTypesConfigurationClass")
@Singleton
public class FigureTypesConfigurationClassDocumentInitializer extends AbstractMandatoryClassInitializer
{
    /**
     * The document reference of the XClass.
     */
    public static final LocalDocumentReference CLASS_REFERENCE =
        new LocalDocumentReference(List.of("XWiki", "Figure"), "FigureTypesConfigurationClass");

    /**
     * The name of field storing the types.
     */
    public static final String BLOCK_TYPES = "blockTypes";

    /**
     * 
     */
    public static final String INLINE_TYPES = "inlineTypes";

    /**
     * Default constructor.
     */
    public FigureTypesConfigurationClassDocumentInitializer()
    {
        super(CLASS_REFERENCE);
    }

    @Override
    protected void createClass(BaseClass xclass)
    {
        super.createClass(xclass);

        xclass.addStaticListField(BLOCK_TYPES, "Block Types", 1, true, false, null, DISPLAYTYPE_INPUT, null, null,
            FREE_TEXT_ALLOWED, false);

        xclass.addStaticListField(INLINE_TYPES, "Inline Types", 1, true, false, null, DISPLAYTYPE_INPUT, null, null,
            FREE_TEXT_ALLOWED, false);
    }
}
