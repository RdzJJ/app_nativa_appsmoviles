package com.habitsapp.ui.viewmodel;

import com.habitsapp.data.repository.HabitRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "KotlinInternalInJava"
})
public final class StatisticsViewModel_Factory implements Factory<StatisticsViewModel> {
  private final Provider<HabitRepository> repositoryProvider;

  public StatisticsViewModel_Factory(Provider<HabitRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public StatisticsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static StatisticsViewModel_Factory create(Provider<HabitRepository> repositoryProvider) {
    return new StatisticsViewModel_Factory(repositoryProvider);
  }

  public static StatisticsViewModel newInstance(HabitRepository repository) {
    return new StatisticsViewModel(repository);
  }
}
