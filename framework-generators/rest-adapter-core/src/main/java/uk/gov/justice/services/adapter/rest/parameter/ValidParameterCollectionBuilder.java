package uk.gov.justice.services.adapter.rest.parameter;

import static java.lang.String.format;

import uk.gov.justice.services.adapter.rest.exception.BadRequestException;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;

/**
 * Validates added query and path parameters and builds an {@link ImmutableMap}.
 *
 * Optional query parameters that are set to null are filtered and not added to the valid map.
 * Required query parameters and path parameters if set to null will throw a {@link
 * BadRequestException}. Repeated parameters names will throw a {@link BadRequestException}.
 */
public class ValidParameterCollectionBuilder implements ParameterCollectionBuilder {

    private static final String PARAM_HAS_NO_VALUE = "The required parameter %s has no value.";
    private static final String INVALID_PARAM_VALUE = "Invalid parameter value.";

    private final Collection<Parameter> parameters = new ArrayList<>();

    private final Logger logger;

    public ValidParameterCollectionBuilder(final Logger logger) {
        this.logger = logger;
    }

    /**
     * returns collection of all valid parameters.
     *
     * @return the collection that contains the valid parameters
     */
    @Override
    public Collection<Parameter> parameters() {
        return parameters;
    }

    /**
     * Add a required parameter to the parameter list.
     *
     * @param name  the parameter name
     * @param value the parameter value
     * @param type  the parameter type
     * @return the current instance of {@link ParameterCollectionBuilder}
     * @throws BadRequestException if the parameter value is null or has invalid value
     */
    @Override
    public ParameterCollectionBuilder putRequired(final String name, final String value, final ParameterType type) {
        if (value == null) {
            throw new BadRequestException(format(PARAM_HAS_NO_VALUE, name));
        }
        addParam(name, value, type);
        return this;
    }

    /**
     * Add an optional parameter to the parameter list
     *
     * @param name  the parameter name
     * @param value the parameter value
     * @param type  the parameter type
     * @return the current instance of {@link ParameterCollectionBuilder}
     * @throws BadRequestException if the parameter has invalid value
     */
    @Override
    public ParameterCollectionBuilder putOptional(final String name, final String value, final ParameterType type) {
        if (value != null) {
            addParam(name, value, type);
        }
        return this;
    }

    private void addParam(final String name, final String value, final ParameterType type) {
        try {
            parameters.add(DefaultParameter.valueOf(name, value, type));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(INVALID_PARAM_VALUE, e);
        }
    }
}