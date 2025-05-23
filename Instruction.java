package sml;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static lombok.AccessLevel.PUBLIC;

import lombok.experimental.Accessors;

/**
 * This class represents an abstract instruction
 *
 */
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
@AllArgsConstructor(access = PUBLIC)
public abstract class Instruction {
    @Getter
    @Setter
    private String label;
    @Getter
    @Setter
    private String opcode;

    public abstract void execute(Machine m);


}
