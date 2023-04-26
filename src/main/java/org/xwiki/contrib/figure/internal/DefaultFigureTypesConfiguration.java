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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.contrib.figure.FigureType;

import static java.util.stream.Collectors.toList;
import static org.xwiki.contrib.figure.FigureType.AUTOMATIC;
import static org.xwiki.contrib.figure.FigureType.FIGURE;
import static org.xwiki.contrib.figure.FigureType.TABLE;
import static org.xwiki.contrib.figure.internal.FigureTypeConfigurationClassDocumentInitializer.TYPES;

/**
 * Default figure types configuration. Resolve the configuration from
 * {@link FigureTypeConfigurationClassDocumentInitializer#CLASS_REFERENCE} found in
 * {@link FigureTypesConfigurationSource#DOCUMENT_REFERENCE}. If not found, resolve the configuration from
 * {@code xwiki.properties} using the {@code figure.types} key. For instance:
 * <pre>
 * figure.types = figure,lemma,proof
 * </pre>
 * {@code figure} and {@code table} are mandatory are will be added if missing.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
public class DefaultFigureTypesConfiguration implements FigureTypesConfiguration
{
    @Inject
    @Named("xwikiproperties")
    private ConfigurationSource propetiesConfigurationSource;

    @Inject
    @Named(FigureTypesConfigurationSource.HINT)
    private ConfigurationSource objectConfigurationSource;

    @Inject
    private Logger logger;

    @Override
    public Set<FigureType> getFigureTypes()
    {
        Set<FigureType> figureTypes = new LinkedHashSet<>();
        figureTypes.add(AUTOMATIC);

        getTypesFromConfiguration()
            .stream()
            .map(String::valueOf)
            .collect(toList())
            .forEach(figureId -> {
                // We make sure that the figure ids are not duplicated.
                if (isNotKnown(figureTypes, figureId)) {
                    figureTypes.add(new FigureType(figureId));
                } else {
                    this.logger.debug("Duplicated figure type [{}] found.", figureId);
                }
            });

        addMandatoryType(figureTypes, FIGURE);
        addMandatoryType(figureTypes, TABLE);

        return figureTypes;
    }

    private List<?> getTypesFromConfiguration()
    {
        if (this.objectConfigurationSource.containsKey(TYPES)) {
            return this.objectConfigurationSource.getProperty(TYPES, List.class);
        } else {
            return this.propetiesConfigurationSource.getProperty("figure.types", List.class);
        }
    }

    private void addMandatoryType(Set<FigureType> figureTypes, FigureType figure)
    {
        if (isNotKnown(figureTypes, figure.getId())) {
            figureTypes.add(figure);
            this.logger.debug("Mandatory type [{}] added.", figure.getId());
        }
    }

    private static boolean isNotKnown(Set<FigureType> figureTypes, String figureId)
    {
        return figureTypes.stream().noneMatch(figureType -> Objects.equals(figureType.getId(), figureId));
    }
}
