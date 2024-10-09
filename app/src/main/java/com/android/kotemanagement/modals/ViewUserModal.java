package com.android.kotemanagement.modals;

import com.android.kotemanagement.room.entities.Soldiers;

import java.util.ArrayList;
import java.util.List;

public class ViewUserModal {
  static public List<Soldiers> viewUserModalList = new ArrayList<>();
  private final String id;
  private final String name;
  private final String rank;
  private final String dateOfJoining;

  public ViewUserModal(String id, String name, String rank, String dateOfJoining) {
    this.id = id;
    this.name = name;
    this.rank = rank;
    this.dateOfJoining = dateOfJoining;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getRank() {
    return rank;
  }

  public String getDateOfJoining() {
    return dateOfJoining;
  }
}
