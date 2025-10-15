package com.habitsapp.ui.screens.edithabit;

import com.habitsapp.data.repository.HabitRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class EditHabitViewModel_Factory implements Factory<EditHabitViewModel> {
  private final Provider<HabitRepository> repositoryProvider;

  private EditHabitViewModel_Factory(Provider<HabitRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public EditHabitViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static EditHabitViewModel_Factory create(Provider<HabitRepository> repositoryProvider) {
    return new EditHabitViewModel_Factory(repositoryProvider);
  }

  public static EditHabitViewModel newInstance(HabitRepository repository) {
    return new EditHabitViewModel(repository);
  }
}
