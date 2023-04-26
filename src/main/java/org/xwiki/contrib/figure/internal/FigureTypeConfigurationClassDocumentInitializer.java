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
import com.xpn.xwiki.objects.classes.ListClass;

/**
 * {@code XWiki.Figure.FigureTypeClass} XClass initializer.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Named("XWiki.Figure.FigureTypeConfigurationClass")
@Singleton
public class FigureTypeConfigurationClassDocumentInitializer extends AbstractMandatoryClassInitializer
{
    /**
     * The document reference of the XClas.
     */
    public static final LocalDocumentReference CLASS_REFERENCE =
        new LocalDocumentReference(List.of("XWiki", "Figure"), "FigureTypeConfigurationClass");

    /**
     * The name of field storing the types.
     */
    public static final String TYPES = "types";

    /**
     * Default constructor.
     */
    public FigureTypeConfigurationClassDocumentInitializer()
    {
        super(CLASS_REFERENCE);
    }

    @Override
    protected void createClass(BaseClass xclass)
    {
        super.createClass(xclass);

        xclass.addStaticListField(TYPES, "Types", 1, true, false,
            null, ListClass.DISPLAYTYPE_INPUT, null, null, ListClass.FREE_TEXT_ALLOWED, false);
    }
}
