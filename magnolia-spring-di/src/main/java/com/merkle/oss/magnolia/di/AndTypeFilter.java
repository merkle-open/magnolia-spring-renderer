package com.merkle.oss.magnolia.di;

import java.io.IOException;
import java.util.Set;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

public class AndTypeFilter implements TypeFilter {
    private final Set<TypeFilter> filters;

    public AndTypeFilter(final Set<TypeFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean match(final MetadataReader metadataReader, final MetadataReaderFactory metadataReaderFactory) throws IOException {
        for (TypeFilter filter : filters) {
            if(!filter.match(metadataReader, metadataReaderFactory)) {
                return false;
            }
        }
        return true;
    }
}
