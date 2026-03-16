# 人脸识别考勤系统后台接口文档

## 1. 用户管理 (User Management)

### 1.1 注册
- **URL**: `/api/user/register`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Description**: 注册新用户 (管理员/普通用户)
- **Request Body**:
  ```json
  {
    "username": "admin",
    "password": "password123",
    "role": "ADMIN" // 或 "USER"
  }
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": "Registered successfully"
  }
  ```

### 1.2 登录
- **URL**: `/api/login`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Description**: 用户登录，获取JWT Token
- **Request Body**:
  ```json
  {
    "username": "admin",
    "password": "password123"
  }
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
  }
  ```

## 2. 员工管理 (Employee Management) - 管理员权限

### 2.1 添加员工
- **URL**: `/api/employee/add`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **Description**: 添加新员工，自动调用Python服务提取特征
- **Parameters**:
  - `name`: 姓名 (String)
  - `gender`: 性别 (1:男, 2:女) (Integer)
  - `idCard`: 身份证号 (String)
  - `address`: 地址 (String)
  - `phone`: 电话 (String)
  - `photo`: 照片文件 (File)
- **Response**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": "Employee added successfully"
  }
  ```

### 2.2 查询所有员工
- **URL**: `/api/employee/list`
- **Method**: `GET`
- **Description**: 获取所有员工列表
- **Response**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": [
      {
        "id": 1,
        "name": "张三",
        "gender": 1,
        "photoUrl": "uuid_filename.jpg",
        "feature": "..."
      }
    ]
  }
  ```

### 2.3 删除员工
- **URL**: `/api/employee/{id}`
- **Method**: `DELETE`
- **Description**: 删除指定ID的员工
- **Response**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": "Deleted successfully"
  }
  ```

## 3. Python端接口 (For Python Client)

### 3.1 获取所有人脸特征
- **URL**: `/api/employee/features`
- **Method**: `GET`
- **Description**: Python端同步人脸特征库
- **Response**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": [
      {
        "id": 1,
        "name": "张三",
        "feature": "base64_encoded_feature_or_array"
      }
    ]
  }
  ```

### 3.2 上传抓拍记录
- **URL**: `/api/capture/save`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Request Body**:
  ```json
  {
    "employeeId": 1, // 陌生人为null
    "imageUrl": "path/to/image.jpg",
    "score": 0.95
  }
  ```
- **Response**:
  ```json
  { "code": 200, "msg": "success", "data": "Saved" }
  ```

### 3.3 上传考勤记录
- **URL**: `/api/attendance/record`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Request Body**:
  ```json
  {
    "employeeId": 1,
    "status": 0 // 0:正常, 1:迟到, 2:早退
  }
  ```
- **Response**:
  ```json
  { "code": 200, "msg": "success", "data": "Recorded" }
  ```

## 4. 前端展示接口 (Frontend)

### 4.1 分页查询抓拍记录
- **URL**: `/api/capture/list`
- **Method**: `GET`
- **Parameters**:
  - `page`: 页码 (默认1)
  - `size`: 每页数量 (默认10)
- **Response**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": [ ... ]
  }
  ```

### 4.2 分页查询考勤记录
- **URL**: `/api/attendance/list`
- **Method**: `GET`
- **Parameters**:
  - `page`: 页码 (默认1)
  - `size`: 每页数量 (默认10)
- **Response**:
  ```json
  {
    "code": 200,
    "msg": "success",
    "data": [ ... ]
  }
  ```

### 4.3 播放录像
- **URL**: `/api/video/play`
- **Method**: `GET`
- **Parameters**:
  - `file`: 视频文件名 (String)
- **Description**: 视频流播放
