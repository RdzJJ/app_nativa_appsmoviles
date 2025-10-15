package com.habitsapp.ui.screens.addhabit;

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
public final class AddHabitViewModel_Factory implements Factory<AddHabitViewModel> {
  private final Provider<HabitRepository> repositoryProvider;

  private AddHabitViewModel_Factory(Provider<HabitRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddHabitViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddHabitViewModel_Factory create(Provider<HabitRepository> repositoryProvider) {
    return new AddHabitViewModel_Factory(repositoryProvider);
  }

  public static AddHabitViewModel newInstance(HabitRepository repository) {
    return new AddHabitViewModel(repository);
  }
}
