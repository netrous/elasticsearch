// Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
// or more contributor license agreements. Licensed under the Elastic License
// 2.0; you may not use this file except in compliance with the Elastic License
// 2.0.
package org.elasticsearch.xpack.esql.expression.function.scalar.multivalue;

import java.lang.Override;
import java.lang.String;
import org.elasticsearch.compute.data.Block;
import org.elasticsearch.compute.data.DoubleBlock;
import org.elasticsearch.compute.data.DoubleVector;
import org.elasticsearch.compute.operator.DriverContext;
import org.elasticsearch.compute.operator.EvalOperator;

/**
 * {@link EvalOperator.ExpressionEvaluator} implementation for {@link MvMin}.
 * This class is generated. Do not edit it.
 */
public final class MvMinDoubleEvaluator extends AbstractMultivalueFunction.AbstractEvaluator {
  private final DriverContext driverContext;

  public MvMinDoubleEvaluator(EvalOperator.ExpressionEvaluator field, DriverContext driverContext) {
    super(field);
    this.driverContext = driverContext;
  }

  @Override
  public String name() {
    return "MvMin";
  }

  /**
   * Evaluate blocks containing at least one multivalued field.
   */
  @Override
  public Block.Ref evalNullable(Block.Ref ref) {
    if (ref.block().mvSortedAscending()) {
      return evalAscendingNullable(ref);
    }
    try (ref) {
      DoubleBlock v = (DoubleBlock) ref.block();
      int positionCount = v.getPositionCount();
      try (DoubleBlock.Builder builder = driverContext.blockFactory().newDoubleBlockBuilder(positionCount)) {
        for (int p = 0; p < positionCount; p++) {
          int valueCount = v.getValueCount(p);
          if (valueCount == 0) {
            builder.appendNull();
            continue;
          }
          int first = v.getFirstValueIndex(p);
          int end = first + valueCount;
          double value = v.getDouble(first);
          for (int i = first + 1; i < end; i++) {
            double next = v.getDouble(i);
            value = MvMin.process(value, next);
          }
          double result = value;
          builder.appendDouble(result);
        }
        return Block.Ref.floating(builder.build());
      }
    }
  }

  /**
   * Evaluate blocks containing at least one multivalued field.
   */
  @Override
  public Block.Ref evalNotNullable(Block.Ref ref) {
    if (ref.block().mvSortedAscending()) {
      return evalAscendingNotNullable(ref);
    }
    try (ref) {
      DoubleBlock v = (DoubleBlock) ref.block();
      int positionCount = v.getPositionCount();
      try (DoubleVector.FixedBuilder builder = driverContext.blockFactory().newDoubleVectorFixedBuilder(positionCount)) {
        for (int p = 0; p < positionCount; p++) {
          int valueCount = v.getValueCount(p);
          int first = v.getFirstValueIndex(p);
          int end = first + valueCount;
          double value = v.getDouble(first);
          for (int i = first + 1; i < end; i++) {
            double next = v.getDouble(i);
            value = MvMin.process(value, next);
          }
          double result = value;
          builder.appendDouble(result);
        }
        return Block.Ref.floating(builder.build().asBlock());
      }
    }
  }

  /**
   * Evaluate blocks containing at least one multivalued field and all multivalued fields are in ascending order.
   */
  private Block.Ref evalAscendingNullable(Block.Ref ref) {
    try (ref) {
      DoubleBlock v = (DoubleBlock) ref.block();
      int positionCount = v.getPositionCount();
      try (DoubleBlock.Builder builder = driverContext.blockFactory().newDoubleBlockBuilder(positionCount)) {
        for (int p = 0; p < positionCount; p++) {
          int valueCount = v.getValueCount(p);
          if (valueCount == 0) {
            builder.appendNull();
            continue;
          }
          int first = v.getFirstValueIndex(p);
          int idx = MvMin.ascendingIndex(valueCount);
          double result = v.getDouble(first + idx);
          builder.appendDouble(result);
        }
        return Block.Ref.floating(builder.build());
      }
    }
  }

  /**
   * Evaluate blocks containing at least one multivalued field and all multivalued fields are in ascending order.
   */
  private Block.Ref evalAscendingNotNullable(Block.Ref ref) {
    try (ref) {
      DoubleBlock v = (DoubleBlock) ref.block();
      int positionCount = v.getPositionCount();
      try (DoubleVector.FixedBuilder builder = driverContext.blockFactory().newDoubleVectorFixedBuilder(positionCount)) {
        for (int p = 0; p < positionCount; p++) {
          int valueCount = v.getValueCount(p);
          int first = v.getFirstValueIndex(p);
          int idx = MvMin.ascendingIndex(valueCount);
          double result = v.getDouble(first + idx);
          builder.appendDouble(result);
        }
        return Block.Ref.floating(builder.build().asBlock());
      }
    }
  }

  public static class Factory implements EvalOperator.ExpressionEvaluator.Factory {
    private final EvalOperator.ExpressionEvaluator.Factory field;

    public Factory(EvalOperator.ExpressionEvaluator.Factory field) {
      this.field = field;
    }

    @Override
    public MvMinDoubleEvaluator get(DriverContext context) {
      return new MvMinDoubleEvaluator(field.get(context), context);
    }

    @Override
    public String toString() {
      return "MvMin[field=" + field + "]";
    }
  }
}