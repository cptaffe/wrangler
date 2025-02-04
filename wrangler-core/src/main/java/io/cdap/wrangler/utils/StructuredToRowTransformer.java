/*
 * Copyright © 2021 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package io.cdap.wrangler.utils;

import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.format.UnexpectedFormatException;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.wrangler.api.Row;

/**
 * Transformer to transform {@link StructuredRecord} to {@link Row}
 */
public class StructuredToRowTransformer {
  private StructuredToRowTransformer() {
  }

  /**
   * Get the row from the given transform.
   *
   * @param record the record to transform
   * @return the row corresponding to the record
   */
  public static Row transform(StructuredRecord record) {
    Row row = new Row();
    for (Schema.Field field : record.getSchema().getFields()) {
      row.add(field.getName(), getValue(record, field.getName()));
    }
    return row;
  }

  /**
   * Get the field value from the given record
   *
   * @param input input record
   * @param fieldName field name to get value from
   * @return the value of the field in the row
   */
  public static Object getValue(StructuredRecord input, String fieldName) {
    Schema fieldSchema = input.getSchema().getField(fieldName).getSchema();
    fieldSchema = fieldSchema.isNullable() ? fieldSchema.getNonNullable() : fieldSchema;
    Schema.LogicalType logicalType = fieldSchema.getLogicalType();

    if (logicalType != null) {
      switch (logicalType) {
        case DATE:
          return input.getDate(fieldName);
        case TIME_MILLIS:
        case TIME_MICROS:
          return input.getTime(fieldName);
        case TIMESTAMP_MILLIS:
        case TIMESTAMP_MICROS:
          return input.getTimestamp(fieldName);
        case DECIMAL:
          return input.getDecimal(fieldName);
        case DATETIME:
          return input.getDateTime(fieldName);
        default:
          throw new UnexpectedFormatException("Field type " + logicalType + " is not supported.");
      }
    }

    // If the logical type is present in complex types, it will be retrieved as corresponding
    // simple type (int/long).
    return input.get(fieldName);
  }
}
