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
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.contrib.figure.FigureStyle;
import org.xwiki.contrib.figure.FigureType;

import static java.util.stream.Collectors.toList;
import static org.xwiki.contrib.figure.FigureStyle.BLOCK;
import static org.xwiki.contrib.figure.FigureStyle.INLINE;
import static org.xwiki.contrib.figure.FigureType.AUTOMATIC;
import static org.xwiki.contrib.figure.FigureType.FIGURE;
import static org.xwiki.contrib.figure.FigureType.TABLE;
import static org.xwiki.contrib.figure.internal.FigureTypesConfigurationClassDocumentInitializer.BLOCK_TYPES;
import static org.xwiki.contrib.figure.internal.FigureTypesConfigurationClassDocumentInitializer.INLINE_TYPES;

/**
 * Default figure types configuration. Resolve the configuration from
 * {@link FigureTypesConfigurationClassDocumentInitializer#CLASS_REFERENCE} found in
 * {@link FigureTypesConfigurationSource#DOCUMENT_REFERENCE}. If not found, resolve the configuration from
 * {@code xwiki.properties} using the {@code figure.block.types} and {@code figure.inline.types} keys. For instance:
 * <pre>
 * figure.block.types = graphs,plots
 * figure.inline.types = proof,lemma,theorem
 * </pre>
 * {@code figure} and {@code table} are mandatory and are always added first.
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
public class DefaultFigureTypesConfiguration implements FigureTypesConfiguration
{
    private static final String PROPERTIES_BLOCK_TYPES = "figure.block.types";

    private static final String PROPERTIES_INLINE_TYPES = "figure.inline.types";

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
        figureTypes.add(FIGURE);
        figureTypes.add(TABLE);

        loadFigureTypes(figureTypes, getBlockTypesFromConfiguration(), BLOCK);
        loadFigureTypes(figureTypes, getInlineTypesFromConfiguration(), INLINE);

        return figureTypes;
    }

    private void loadFigureTypes(Set<FigureType> figureTypes, List<Object> blockTypesFromConfiguration,
        FigureStyle block)
    {
        if (blockTypesFromConfiguration == null) {
            return;
        }
        blockTypesFromConfiguration
            .stream()
            .map(String::valueOf)
            .collect(toList())
            .forEach(figureId -> {
                // We make sure that the figure ids are not duplicated.
                if (isNotKnown(figureTypes, figureId)) {
                    figureTypes.add(new FigureType(figureId, block));
                } else {
                    this.logger.debug("Duplicated figure type [{}] found.", figureId);
                }
            });
    }

    @Override
    public Optional<FigureType> getFigureType(String id)
    {
        return getFigureTypes()
            .stream()
            .filter(figureType -> Objects.equals(figureType.getId(), id))
            .findAny();
    }

    @Override
    public FigureStyle getFigureStyle(String id)
    {
        return getFigureType(id).map(FigureType::getFigureStyle).orElse(BLOCK);
    }

    private <T> List<T> getBlockTypesFromConfiguration()
    {
        return getFromConfiguration(BLOCK_TYPES, PROPERTIES_BLOCK_TYPES);
    }

    private <T> List<T> getInlineTypesFromConfiguration()
    {
        return getFromConfiguration(INLINE_TYPES, PROPERTIES_INLINE_TYPES);
    }

    private <T> List<T> getFromConfiguration(String xObjectKey, String propertiesKey)
    {
        if (!this.objectConfigurationSource.isEmpty()) {
            return this.objectConfigurationSource.getProperty(xObjectKey, List.class);
        } else {
            return this.propetiesConfigurationSource.getProperty(propertiesKey, List.class);
        }
    }

    private static boolean isNotKnown(Set<FigureType> figureTypes, String figureId)
    {
        return figureTypes.stream().noneMatch(figureType -> Objects.equals(figureType.getId(), figureId));
    }
}
