/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.andbase.library.asynctask;

import java.util.List;

/**
 * Copyright amsoft.cn
 * Author 还如一梦中
 * Date 2016/6/14 17:54
 * Email 396196516@qq.com
 * Info 数据监听器
 */
public abstract class AbTaskListListener extends AbTaskListener {

	/**
	 * 执行开始.
	 * 
	 * @return 返回的结果列表
	 */
	public abstract List<?> getList();

	/**
	 * 执行完成后回调. 不管成功与否都会执行
	 * 
	 * @param paramList 返回的List
	 */
	public abstract void update(List<?> paramList);

}
