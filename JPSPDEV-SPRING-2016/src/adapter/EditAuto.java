package adapter;

import scale.EditOptionEnum;

/* Edit the automobile with multiple threads
 * update option set name*/

public interface EditAuto {

	public void edit(EditOptionEnum editOptionEnum, String[] args);
}
