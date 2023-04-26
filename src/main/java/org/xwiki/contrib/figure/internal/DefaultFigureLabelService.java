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

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.xwiki.component.annotation.Component;
import org.xwiki.localization.ContextualLocalizationManager;

/**
 * Default implementation of {@link FigureLabelService}. Search for a translation, and return the type capitalized if no
 * translation is found.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
public class DefaultFigureLabelService implements FigureLabelService
{
    @Inject
    private ContextualLocalizationManager contextLocalization;

    @Override
    public String getLabel(String type)
    {
        String translationKeyFormat = "org.xwiki.rendering.macro.figure.FigureType.%s";
        String key = String.format(translationKeyFormat, type);
        String label;
        if (this.contextLocalization.getTranslation(key) != null) {
            label = this.contextLocalization.getTranslationPlain(key);
        } else {
            String capitalizedKey = String.format(translationKeyFormat, type.toUpperCase());
            if (this.contextLocalization.getTranslation(capitalizedKey) != null) {
                label = this.contextLocalization.getTranslationPlain(capitalizedKey);
            } else {
                label = StringUtils.capitalize(type);
            }
        }
        return label;
    }
}
