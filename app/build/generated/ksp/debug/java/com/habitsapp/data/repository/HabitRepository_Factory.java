package com.habitsapp.data.repository;

import com.habitsapp.data.database.HabitDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class HabitRepository_Factory implements Factory<HabitRepository> {
  private final Provider<HabitDao> habitDaoProvider;

  public HabitRepository_Factory(Provider<HabitDao> habitDaoProvider) {
    this.habitDaoProvider = habitDaoProvider;
  }

  @Override
  public HabitRepository get() {
    return newInstance(habitDaoProvider.get());
  }

  public static HabitRepository_Factory create(Provider<HabitDao> habitDaoProvider) {
    return new HabitRepository_Factory(habitDaoProvider);
  }

  public static HabitRepository newInstance(HabitDao habitDao) {
    return new HabitRepository(habitDao);
  }
}
