/*
 * Copyright (c) 2019 Spotify AB.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.spotify.heroic.aggregation.cardinality

import com.spotify.heroic.ObjectHasher
import com.spotify.heroic.aggregation.BucketAggregationInstance
import com.spotify.heroic.metric.MetricType
import com.spotify.heroic.metric.Payload

data class DistributedCardinalityInstance(
        override val size: Long,
        override val extent: Long,
        val method: CardinalityMethod
) : BucketAggregationInstance<CardinalityBucket>(size, extent, ALL_TYPES, MetricType.CARDINALITY) {

    override fun buildBucket(timestamp: Long): CardinalityBucket {
        return method.build(timestamp)
    }

    override fun build(bucket: CardinalityBucket): Payload {
        return Payload(bucket.timestamp, bucket.state())
    }

    override fun bucketHashTo(hasher: ObjectHasher) {
        hasher.putField("method", method, hasher.with(CardinalityMethod::hashTo))
    }

    companion object {
        const val NAME = "distributed-cardinality"
    }
}
