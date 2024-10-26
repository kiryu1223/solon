/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.solon.data.sqlink.base.expression;

import org.noear.solon.data.sqlink.base.IConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ISqlCollectedValueExpression extends ISqlValueExpression
{
    Collection<Object> getCollection();

    void setDelimiter(String delimiter);

    String getDelimiter();

    @Override
    default ISqlCollectedValueExpression copy(IConfig config)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<Object> newValues = new ArrayList<>(getCollection());
        ISqlCollectedValueExpression value = factory.value(newValues);
        value.setDelimiter(getDelimiter());
        return value;
    }
}
