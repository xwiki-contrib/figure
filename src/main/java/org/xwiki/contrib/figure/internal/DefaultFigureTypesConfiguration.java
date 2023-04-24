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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.collections4.SetUtils;
import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.phase.Initializable;
import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.contrib.figure.FigureType;
import org.xwiki.properties.ConverterManager;
import org.xwiki.properties.converter.Converter;

import static org.xwiki.contrib.figure.FigureType.AUTOMATIC;
import static org.xwiki.contrib.figure.FigureType.FIGURE;
import static org.xwiki.contrib.figure.FigureType.TABLE;

/**
 * Default figure type configuration. Resolve the configuration from {@code xwiki.properties} using the
 * {@code figure.types} key. For instance:
 * <pre>
 * figure.types = figure = figure
 * figure.types = math = lemma\,proof
 * </pre>
 *
 * @version $Id$
 * @since 15.4
 */
@Component
@Singleton
public class DefaultFigureTypesConfiguration implements FigureTypesConfiguration, Initializable
{
    @Inject
    @Named("xwikiproperties")
    private ConfigurationSource configurationSource;

    @Inject
    private ConverterManager converterManager;

    @Inject
    private Logger logger;

    private Converter<List<?>> listConverter;

    @Override
    public void initialize()
    {
        this.listConverter = this.converterManager.getConverter(List.class);
    }

    @Override
    public Set<FigureType> getFigureTypes()
    {
        return getFigureTypes(getConfigurationMap());
    }

    @Override
    public Map<String, Set<FigureType>> getFigureCounters()
    {
        Map<String, Set<String>> configurationMap = getConfigurationMap();
        Map<String, Set<FigureType>> counters = new HashMap<>();
        for (FigureType figureType : getFigureTypes(configurationMap)) {
            if (isAutomatic(figureType)) {
                continue;
            }
            counters.merge(computeCounter(configurationMap, figureType), Set.of(figureType), SetUtils::union);
        }
        return counters;
    }

    @Override
    public String getCounter(String type)
    {
        return computeCounter(getConfigurationMap(), new FigureType(type));
    }

    /**
     * Internal figure types, which a provided configuration map. Used internally to avoid loading and parsing the
     * configuration several times.
     *
     * @param configurationMap the configuration map to use to resolve the set of figure types
     * @return the resolved set of figure types
     */
    private Set<FigureType> getFigureTypes(Map<String, Set<String>> configurationMap)
    {
        Set<String> keys = configurationMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
        Set<FigureType> figureTypes = new HashSet<>();
        figureTypes.add(AUTOMATIC);
        figureTypes.add(FIGURE);
        figureTypes.add(TABLE);

        // TODO: add a notion of sorting? Maybe client side by translated label?

        keys.forEach(figureId -> {
            // We make sure that the figure ids are not duplicated.
            boolean idAlreadyKnown =
                figureTypes.stream().anyMatch(figureType -> Objects.equals(figureType.getId(), figureId));
            if (!idAlreadyKnown) {
                figureTypes.add(new FigureType(figureId));
            } else {
                this.logger.debug("Duplicated figure type [{}] found.", figureId);
            }
        });

        return figureTypes;
    }

    private Map<String, Set<String>> getConfigurationMap()
    {
        Properties properties = this.configurationSource.getProperty("figure.types", Properties.class);
        Map<String, Set<String>> map = new HashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            map.put((String) entry.getKey(), new HashSet<>(this.listConverter.convert(List.class, entry.getValue())));
        }

        return map;
    }

    private static boolean isAutomatic(FigureType figureType)
    {
        return Objects.equals(figureType.getId(), AUTOMATIC.getId());
    }

    private String computeCounter(Map<String, Set<String>> configurationMap, FigureType figureType)
    {
        return computeCounter(configurationMap, figureType.getId());
    }

    private String computeCounter(Map<String, Set<String>> configurationMap, String figureType)
    {
        List<String> collect = configurationMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue().contains(figureType))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        String computedCounter;
        if (collect.size() > 1) {
            this.logger.debug("Type [{}] appears in more than one counter [{}]. Taking the first one.", figureType,
                collect);
            computedCounter = collect.get(0);
        } else if (collect.size() == 1) {
            computedCounter = collect.get(0);
        } else {
            // If no counter is configured for a given
            computedCounter = figureType;
        }
        return computedCounter;
    }
}
