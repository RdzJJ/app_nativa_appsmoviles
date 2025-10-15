package com.habitsapp.ui.viewmodel;

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
public final class HabitsViewModel_Factory implements Factory<HabitsViewModel> {
  private final Provider<HabitRepository> repositoryProvider;

  private HabitsViewModel_Factory(Provider<HabitRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public HabitsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static HabitsViewModel_Factory create(Provider<HabitRepository> repositoryProvider) {
    return new HabitsViewModel_Factory(repositoryProvider);
  }

  public static HabitsViewModel newInstance(HabitRepository repository) {
    return new HabitsViewModel(repository);
  }
}
