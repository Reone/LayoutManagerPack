/**
 * 包含一些自定义的layoutManager
 * 遵循原则：
 * 1. 可单独使用：每个layoutManager目录复制出去可单独使用
 * 2. 使用简单：使用时需要如LinearLayoutManager般简单使用，如果需要特殊操作需要在debug时警告使用者
 * 3. 不操作数据：如果需要，提供必须实现的接口给使用者
 * 4. 功能注释：每一个自定义的LayoutManager要在文件中提供详细注释（详细功能，或使用场景，或模仿对象）
 * 5. 使用注释：如果需要特殊操作需要提供相应注释
 */
package com.reone.layoutmanagerpkg;