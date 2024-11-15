//package com.android.kotemanagement.fragments.soldiers;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.android.kotemanagement.R;
//import com.android.kotemanagement.activities.UpdateUsersActivity;
//import com.android.kotemanagement.databinding.ViewUserListItemLayoutBinding;
//import com.android.kotemanagement.utilities.ConvertImage;
//
//import java.util.concurrent.Executors;
//
//public class ViewSoldiersDialogFragment extends DialogFragment {
//
//    private ViewUserListItemLayoutBinding binding;
//    private Bitmap selectedImage;
//    String image;
//    String firstName;
//    String lastName;
//    String rank;
//    String armyNumber;
//    String dob;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        binding = ViewUserListItemLayoutBinding.inflate(inflater, container, false);
//
//        if(getDialog() != null && getDialog().getWindow() != null) {
//            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        }
//
//        getAndSetData();
//
//        binding.btnUpdateUser.setOnClickListener(v -> {
//            getParentFragmentManager().beginTransaction().remove(this).commit();
//
//            Intent intent = new Intent(requireActivity(), UpdateUsersActivity.class);
//            intent.putExtra("army_number", armyNumber);
//            startActivity(intent);
//
//        });
//
//        return binding.getRoot();
//    }
//
//    private void getAndSetData() {
//        Bundle arguments = getArguments();
//
//        if(arguments != null) {
//            image = arguments.getString("image");
//            firstName = arguments.getString("firstName");
//            lastName = arguments.getString("lastName");
//            rank = arguments.getString("rank");
//            armyNumber = arguments.getString("armyNumber");
//            dob = arguments.getString("dob");
//        }
//
//        selectedImage = ConvertImage.convertToBitmap(image);
//
//        binding.civSoldiers.setImageBitmap(selectedImage);
//        binding.tvName.setText(firstName + " " + lastName);
//        binding.tvRank.setText(rank);
//        binding.tvID.setText(armyNumber);
//        binding.tvDOB.setText(dob);
//
//    }
//
//}