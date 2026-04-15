package com.github.saikos.security.authorization;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.Authorizer;
import com.github.saikos.security.EcosystemEnvironment;
import com.github.saikos.security.Subject;
import com.github.saikos.security.Subsystem;
import com.github.saikos.security.capabilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.Predicate;

public class Rule<
    S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>,
    C extends Capability<S>
> implements Authorizer<S> {

    private static final Logger logger = LoggerFactory.getLogger(Rule.class);

    public final RuleType type;
    // The name of the capability for which the rule is written.
    // If the name is null, then this rule applies to all capabilities.
    public final String applicableForCapability;
    public final String description;
    public final Predicate<C> condition;
    public final Function<Capability<S>, C> transformer;

    public Rule(RuleType type,
                String applicableForCapability,
                String description,
                Predicate<C> condition,
                Function<Capability<S>, C> transformer) {
        this.type = type;
        this.applicableForCapability = applicableForCapability;
        this.description = description;
        this.condition = condition;
        this.transformer = transformer;
    }

    public Rule(RuleType type,
                String applicableForCapability,
                Predicate<C> condition) {
        this(type, applicableForCapability, applicableForCapability, condition, null);
    }

    public Rule(RuleType type,
                String applicableForCapability,
                String description,
                Predicate<C> condition) {
        this(type, applicableForCapability, description, condition, null);
    }

    public boolean conditionApplies(Capability<S> capability)
            throws CapabilityConditionException, CapabilityValidationException {
        if (capability == null) {
            return false;
        }
        else {
            try {
                return condition.test((C) capability);
            }
            catch(CapabilityValidationException cve) {
                throw cve;
            }
            catch(Exception e) {
                throw new CapabilityConditionException(e, capability);
            }
        }
    }

    @Override
    public <C extends Capability<S>> C authorize(C capability)
            throws CapabilityDeniedException, CapabilityTransformException {
        if (type == RuleType.GRANT) {
            logger.info("Rule {} grants capability {} ",this.toString(), capability.toString());
            return capability;
        }
        else if (type == RuleType.DENY) {
            logger.info("Rule {} denies capability {} ",this.toString(), capability.toString());
            throw new CapabilityDeniedException(capability);
        }
        else {
            try {
                logger.info("Rule {} transforms capability {} ",this.toString(), capability.toString());
                return transformer == null ? capability : ((Function<Capability<S>, C>) transformer).apply(capability);
            }
            catch(Exception e) {
                throw new CapabilityTransformException(e, capability);
            }
        }
    }

    @Override
    public String toString() {
        return "%s [ type: %s, capabilityClass: %s, condition: %s, transformer: %s]".formatted(description, type, applicableForCapability, condition, transformer);
    }
}
