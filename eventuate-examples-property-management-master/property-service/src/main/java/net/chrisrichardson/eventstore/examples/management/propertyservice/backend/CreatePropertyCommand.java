package net.chrisrichardson.eventstore.examples.management.propertyservice.backend;

import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import org.apache.commons.lang.builder.ToStringBuilder;

public class CreatePropertyCommand  implements PropertyCommand {
    private PropertyInfo propertyInfo;

    public CreatePropertyCommand() {
    }

    public CreatePropertyCommand(PropertyInfo propertyInfo) {
        this.propertyInfo = propertyInfo;
    }

    public PropertyInfo getPropertyInfo() {
        return propertyInfo;
    }

    public void setPropertyInfo(PropertyInfo propertyInfo) {
        this.propertyInfo = propertyInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
