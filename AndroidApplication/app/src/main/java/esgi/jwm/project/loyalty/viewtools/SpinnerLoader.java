package esgi.jwm.project.loyalty.viewtools;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;

public class SpinnerLoader {

//    TODO Try to do a SpinnerLoader class which can Load spinner data in any Spinner of the application

//    Une classe qui permet de charger facilement les spinners (spinner = liste deroulante)


//    public void loadAllSpiner(ArrayList<View> viewArrayList, Intent data){
//        for(View view: viewArrayList){
//            loadSpinner(view, data);
//        }
//    }
//

//
//    public void loadSpinner(Spinner sp, DatabaseHelper dh, Project currentProject, Context context){
//        switch (sp.getId()){
//            case R.id.spinnerTaskList:
//                ArrayList<TaskList> arrayListTaskList = dh.getAllTasklistOfProject(currentProject.getId());
//                ArrayList<String> spinnerTaskListArray = new ArrayList<>();
//
//                for (TaskList taskList :arrayListTaskList) {
//                    spinnerTaskListArray.add(taskList.getTaskListName());
//                }
//
//                ArrayAdapter<String> adapterTaskList = new ArrayAdapter<String>(context, R.layout.my_spinner_item, spinnerTaskListArray);
//                adapterTaskList.setDropDownViewResource(R.layout.my_spinner_item);
//                sp.setAdapter(adapterTaskList);
//
////                return sp;
//                break;
//
//            case R.id.spinnerCriticity:
//
//                ArrayList<Integer> spinnerCriticityArray = new ArrayList<>();
//                for(int i = 0; i < 5; i++){
//                    spinnerCriticityArray.add(i+1);
//                }
//
//                ArrayAdapter<Integer> adapterCriticity = new ArrayAdapter<Integer>(context, R.layout.my_spinner_item, spinnerCriticityArray);
////                adapterCriticity.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//                sp.setAdapter(adapterCriticity);
//
//                break;
////                return sp;
//
//            default:
//                break;
//
//        }
//    }
}
