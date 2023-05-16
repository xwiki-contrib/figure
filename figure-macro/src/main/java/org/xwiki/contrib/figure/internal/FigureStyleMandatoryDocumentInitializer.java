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

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.LocalDocumentReference;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.AbstractMandatoryDocumentInitializer;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;

import static com.xpn.xwiki.XWiki.SYSTEM_SPACE;

/**
 * Initialize the style sheet extension XObject for the figure macro.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Named("XWiki.Figure.FigureStyle")
@Singleton
public class FigureStyleMandatoryDocumentInitializer extends AbstractMandatoryDocumentInitializer
{
    /**
     * Document reference of the document containing the figure style sheet XObject.
     */
    public static final LocalDocumentReference FIGURE_STYLE_DOCUMENT_REFERENCE =
        new LocalDocumentReference(List.of(SYSTEM_SPACE, "Figure"), "FigureStyle");

    private static final String FIGURE_LESS_PATH = "figure.less.vm";

    @Inject
    private Provider<XWikiContext> contextProvider;

    @Inject
    private Logger logger;

    /**
     * Default constructor.
     */
    public FigureStyleMandatoryDocumentInitializer()
    {
        super(FIGURE_STYLE_DOCUMENT_REFERENCE);
    }

    @Override
    public boolean updateDocument(XWikiDocument document)
    {
        boolean needsUpdate = super.updateDocument(document);

        DocumentReference ssxDocRef =
            new DocumentReference(this.wikiDescriptorManager.getCurrentWikiId(), SYSTEM_SPACE,
                "StyleSheetExtension");

        BaseObject styleSheetExtensionXObject = document.getXObject(ssxDocRef);
        if (styleSheetExtensionXObject == null) {
            try {
                styleSheetExtensionXObject = document.newXObject(ssxDocRef, this.contextProvider.get());

                styleSheetExtensionXObject.setLargeStringValue("code", readCode());
                styleSheetExtensionXObject.setStringValue("use", "onDemand");
                styleSheetExtensionXObject.setIntValue("parse", 1);
                styleSheetExtensionXObject.setStringValue("cache", "long");
                styleSheetExtensionXObject.setStringValue("contentType", "LESS");

                needsUpdate = true;
            } catch (XWikiException e) {
                this.logger.error("Failed to create XObject [{}]", ssxDocRef, e);
            } catch (IOException e) {
                this.logger.error(String.format("Failed to read file [%s]", FIGURE_LESS_PATH), e);
            }
        }

        return needsUpdate;
    }

    private String readCode() throws IOException
    {
        return IOUtils.toString(getClass().getResourceAsStream(String.format("/%s", FIGURE_LESS_PATH)));
    }
}
