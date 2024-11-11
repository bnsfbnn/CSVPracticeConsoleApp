# Java Fresher 02: Mock 1

## Kiến trúc tổng quan

Dự án sử dụng kiến trúc phân tầng (**Layered Architecture**). 3 tầng chính là 3 tầng đóng, yêu cầu request đi theo thứ
tự từ tầng trên xuống tầng dưới. 3 tầng chính bao gồm:

### Presentation Layer

- **Chức năng**: Tầng này bao gồm một interface `IBaseFunction` chính để nhận `function code` và folder path chứa các
  file
  input và output. Các `Handler` khác nhau sẽ thực hiện các chức năng tùy theo `function code` đã nhận.

- **Giao tiếp**: Tầng này chỉ tiếp xúc với `Business Layer` thông qua các `Service`.

### Business Layer

- **Chức năng**: Tầng này có các `Service Interface` chứa các phương thức như `loadFile`, `writeFile`, `insert`,
  `update`, `delete` và các lớp `Service Implement` thực hiện logic riêng cho từng thực thể.

- **Giao tiếp**: Tầng này chỉ tiếp xúc với `Data Access Layer` thông qua các instance của `DataLoader` và `DataWriter`.

### Data Access Layer

- **Chức năng**: Bao gồm các `entity` của dự án, các `DTO` và các lớp thao tác với dữ liệu. Trong `datahandler` có hai
  lớp chính là `DataLoader` và `DataWriter`, được viết dưới dạng `Generic` và gọi các `Functional Interface` riêng cho
  mỗi `Entity`.

- **Chi tiết**: Các hàm này được định nghĩa trong lớp `DataLineParser` và `DataLineWriter`.

### Infrastructure Layer (Tầng Hạ tầng)

- **Mô tả**: Đây là tầng đặc biệt có thể được truy cập từ tất cả các tầng khác.
- **Chức năng**: Bao gồm các constants, các lớp utility, và các validator.

### Project Structure

```
com.ntq.training
│
├── bl                               # package chứa Business Logic
│   ├── IDataService                 # Interface chứa phương thức loadFile và saveFile
│   ├── CustomerService              # Interface chứa phương thức quản lý dữ liệu khách hàng
│   ├── ProductService               # Interface chứa phương thức quản lý dữ liệu sản phẩm
│   ├── OrderService                 # Interface chứa phương thức quản lý dữ liệu đơn hàng
│   └── impl                         # Triển khai các phương thức từ `Service`
│       ├── CustomerServiceImpl      # Triển khai các phương thức quản lý dữ liệu khách hàng
│       ├── ProductServiceImpl       # Triển khai các phương thức quản lý dữ liệu sản phẩm
│       └── OrderServiceImpl         # Triển khai các phương thức quản lý dữ liệu đơn hàng
│
├── dal                              # package chứa Data Access Layer
│   ├── datahandler                  # 
│       ├── DataLineParser           # Chứa các triển khai của function interface parser cho từng lớp 
│       ├── DataLineWriter           # Chứa các triển khai của các function interface liên quan đến việc ghi dữ liệu
│       ├── DataWriter               # Chứa 1 phương thức duy nhất thực hiên việc write dữ liệu
│       └── DataLoader               # Chứa 1 phương thức duy nhất thực hiên việc load dữ liệu
│   ├── dto                          # package chứa Data Transfer Object
│   └── entity                       # package chứa Entity
│
├── pl                               # package chứa Presentation Layer (responsible for file I/O operations)
│   ├── IBaseFunction                # Interface định nghĩa các phương thức nhận `function code` và `file path`
│   ├── CommonDataHandler            # Service chung cho việc load và write data với mỗi entity khác nhau
│   └── impl                         # Triển khai các phương thức từ `IBaseFunction`
│       ├── CustomerServiceImpl      # 
│       └── .....                    # 
│
├── infra                            # package chứa Infracstructure
│   ├── constants                    # package chứa hằng số 
│   ├── util                         # package chứa các lớp utility
│   └── validator                    # package chứa các lớp validator (Parser, Unique, File
```

## Luồng validate file

### 4 giai đoạn validate file

1. **File Validator**:
    - **Gọi từ**: `FileReaderHelper` và `FileWriterHelper`.
    - **Chức năng**: Được thực hiện ngay sau khi đọc dữ liệu từ file CSV. Loại bỏ các dòng thiếu giá trị của field (giá
      trị bị `null` hoặc `empty`).
    - **Kết quả**: Trả về `Map<Integer, List<String>>`, trong đó `key` đại diện cho số dòng (row number) và `value` là
      danh sách các giá trị của dòng đó.

2. **Parser Validator**:
    - **Gọi từ**: `DataLoader`.
    - **Chức năng**: Thực hiện sau khi parse dữ liệu từ danh sách `String` thành danh sách đối tượng. Loại bỏ các dòng
      có field chứa dữ liệu không đúng kiểu hoặc không đáp ứng yêu cầu (vd: số âm, định dạng sai của `phone`, `email`,
      `order date`, `product quantities`).
    - **Kết quả**: Trả về `Map<Integer, List<T>>`, trong đó `key` đại diện cho số dòng (row number) và `value` là danh
      sách các đối tượng hợp lệ của dòng đó.

3. **Unique Validator**:
    - **Gọi từ**: Các `Service`.
    - **Chức năng**: Thực hiện sau khi đã lấy danh sách đối tượng thỏa mãn yêu cầu định dạng, loại bỏ các field bị trùng
      lặp theo thứ tự từ trên xuống dưới.
    - **Kết quả**: Trả về `Map<Integer, List<T>>`.

4. **Consistency Validator**:
    - **Gọi từ**: Các `Handler`.
    - **Chức năng**: Thực hiện sau khi đã load đầy đủ danh sách tất cả các đối tượng. Đảm bảo tính nhất quán của danh
      sách đối tượng phụ thuộc vào các danh sách đối tượng khác (vd: `Order` yêu cầu `customerId` và `productId` phải
      tồn tại).

### Dữ liệu kiểm tra validate

`products.orgin.csv`

```csv
Id,Name,Price,StockAvailable
,Túi LV,3000.0,567
P1001,,3000.0,567
P1001,Túi LV,,567
P1001,Túi LV,3000.0,
Túi LV,3000.0,567
P1000,Túi xách da,981.0,826
P1001,Túi xách da,-1,826
P1001,Túi xách da,1000,826.00
P1001,Túi xách da,1000,-826
P1001,Túi xách da,0,826
,,3000.0,567
```

`customers.orgin.csv`

```csv
Id,Name,Email,PhoneNumber
,Nguyễn Thanh Tùng,w4q6d333@gmail.com,0328060333
,,w4q6d333@gmail.com,0328060333
CUS5000,,w4q6d111@gmail.com,0328060111
CUS5000,Nguyễn Thanh Tùng,,0328060111
CUS5000,Nguyễn Thanh Tùng,w4q6d111@gmail.com,
CUS5000,Nguyễn Hạnh Lan,w4q6d333@gmail.com,0328060333
CUS5000,Nguyễn Hạnh Lan,w4q6d321@@gmail.vn,03280605233
CUS5000,Nguyễn Hạnh Lan,w4q6d321@gmail.com,03280605233
CUS5000,Nguyễn Hạnh Lan,w4q6d321gmail.com,03280605233
CUS5000,Nguyễn Hạnh Lan,w4q6d321@gmail.com.vn,0329060523
CUS5001,Nguyễn Hạnh Lan,w4q6d321@gmail.com.vn,0328060523
CUS5001,Nguyễn Hạnh Lan,w4q6d321@gmail.com,0329060524
```

`orders.orgin.csv`

```csv
Id,CustomerID,ProductQuantities,OrderDate
,CUS1746,P0600:47,2024-10-06T23:38:36.049569+07:07
ORD4999,,P0600:47,2024-10-06T23:38:36.049569+07:07
ORD4999,CUS1746,,2024-10-06T23:38:36.049569+07:07
ORD4999,CUS1746,P0600:47,
,,P0600:47,2024-10-06T23:38:36.049569+07:07
,P0600:47,2024-10-06T23:38:36.049569+07:07
,P0600:47,
ORD4999,CUS1746,P0600:-47;P0500:57,2024-10-06T23:38:36.049569+07:07
ORD4999,CUS1746,P0600:a;P0500:b,2024-10-06T23:38:36.049569+07:07
ORD4999,CUS1746,P0600;47:P0500;57,2024-10-06T23:38:36.049569+07:07
ORD4999,CUS1746,P0600:40;P0600:40,2024-10-06T23:38:36.049569+07:07
ORD5000,CUS9999,P0600:40;P0500:40,2024-10-06T23:38:36.049569+07:07
ORD5000,CUS1222,P9999:40;P9998:40,2024-10-06T23:38:36.049569+07:07
```
