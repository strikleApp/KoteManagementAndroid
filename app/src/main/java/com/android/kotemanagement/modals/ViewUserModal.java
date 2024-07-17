package com.android.kotemanagement.modals;

public class ViewUserModal {
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
