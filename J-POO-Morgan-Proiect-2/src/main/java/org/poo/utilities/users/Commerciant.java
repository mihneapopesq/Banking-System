package org.poo.utilities.users;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommerciantInput;

@Getter
@Setter
public class Commerciant {
    CommerciantInput commerciant;
    public Commerciant() {
        this.commerciant = new CommerciantInput();
    }

}
