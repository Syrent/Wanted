package ir.syrent.wanted.GUI.pagination;

import ir.syrent.wanted.GUI.SGMenu;
import ir.syrent.wanted.GUI.buttons.SGButton;

public interface SGPaginationButtonBuilder {

    SGButton buildPaginationButton(SGPaginationButtonType type, SGMenu inventory);

}
