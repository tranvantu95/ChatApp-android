package com.ging.chat.activity.base;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ging.chat.R;
import com.ging.chat.config.AppData;
import com.ging.chat.config.Debug;
import com.ging.chat.model.AppModel;
import com.ging.chat.model.base.SwitchListModel;
import com.ging.chat.utils.AppUtils;
import com.ging.chat.utils.General;

public abstract class SwitchListActivity extends BaseActivity {

    private Menu typeViewMenu;

    private int typeView;

    @Override
    protected void onStart() {
        super.onStart();

        setTypeView(General.typeView);
    }

    private void setTypeView(int typeView) {
        if(this.typeView == typeView) return;
        this.typeView = typeView;
        
        if(General.typeView != typeView) {
            General.typeView = typeView;
            AppData.getInstance().saveTypeView(General.typeView);
            getAppModel(AppModel.class).getTypeView().setValue(General.typeView);
        }

        Log.d(Debug.TAG + TAG, "onChangeTypeView " + typeView);
        onChangeTypeView(typeView);

        setChecked(typeView);
    }

    protected abstract void onChangeTypeView(int typeView);

    private void setChecked(int typeView) {
        if(typeViewMenu == null) return;
        Log.d(Debug.TAG + TAG, "setChecked " + typeView);
        AppUtils.clearChecked(typeViewMenu);
        AppUtils.setChecked(typeViewMenu.findItem(getTypeViewMenuItemId(typeView)), true);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        boolean b = super.onPrepareOptionsMenu(menu);
        
        MenuItem typeViewMenuItem = menu.findItem(R.id.type_view);
        
        if(typeViewMenuItem != null) {
            Menu typeViewMenu = typeViewMenuItem.getSubMenu();
            if(this.typeViewMenu != typeViewMenu) {
                this.typeViewMenu = typeViewMenu;
                setChecked(typeView);
            }
        }

        return b;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.type_list:
            case R.id.type_grid: {
                if(item.isChecked()) return true;
                setTypeView(getTypeView(id));
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int getTypeView(int menuItemId) {
        switch (menuItemId) {
            case R.id.type_list:
                return SwitchListModel.LIST;

            case R.id.type_grid:
                return SwitchListModel.GRID;

            default:
                return SwitchListModel.LIST;
        }
    }

    private int getTypeViewMenuItemId(int typeView) {
        switch (typeView) {
            case SwitchListModel.LIST:
                return R.id.type_list;

            case SwitchListModel.GRID:
                return R.id.type_grid;

            default:
                return R.id.type_list;
        }
    }

}
