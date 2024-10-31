#Java Fresher 02: Mock 1

## Kiến trúc tổng quan
Dự án sử dụng kiến trúc phân tầng (Layered Architecture). Mỗi 3 tầng chính là tầng đóng, request cần đi theo thứ tự từ
layer trên xuống layer dưới. Dự án bao gồm 3 tầng chính bao gồm:

- **Presentation Layer: Bao gồm 1 IBaseController chính nhận function code và các controllers khác implement tùy theo
function code đã nhận. Tầng này chỉ tiếp xúc với tầng Business Layer thông qua các Service.

- **Business Layer: Bao gồm 1 Base Service Interface trong đó có các method (loadFile, writeFile, insert, update,
delete) và các Implement Service thực hiện logic riêng cho từng thực thể. Tầng này chỉ tiếp xúc với tầng Data Access
Layer thông qua các instance DataLoader và DataWriter.

- **Data Access Layer: Bao gồm các entity của dự án và các lớp thao tác với dữ liệu. Trong datahandler bao gồm 2 class
chính (DataLoader và DataWriter) được viết Generic và gọi các Functional Interface riêng cho mỗi Entity (các hàm này
được viết trong lớp DataLineParser và DataLineWriter).

Ngoài 3 tầng chính. Còn layer khác được gọi là Infrastructure Layer, layer được truy cập từ tất cả các layer khác. Bao
gồm các constants, các lớp utility, và các validator.

## Luồng validate file:
- *1File Validator: Được gọi từ FileReaderHelper và FileWriterHelper. Thực hiện ngay khi đọc được dữ liệu từ file csv.
Sẽ thực hiện loại bỏ các dòng thiếu giá trị của field (giá trị field bị null hoặc empty). Trả về
Map<Integer, List<String>> trong đó key đại diện cho row number trong file csv và value

- *2Parser Validator: Được gọi bởi DataLoader. Thực hiện sau khi parse dữ liệu từ danh sách String thành danh sách đối
tượng. Sẽ thực hiện loại bỏ các dòng có các field có dữ liệu không đúng kiểu dữ liệu, ko đúng yêu cầu (vd: số âm, ko
đúng định dạng phone, email, order date, product quantities). Trả về Map<Integer, List<T>> trong đó key đại diện cho
row number trong file csv và value

- *3Unique Validator: Được gọi bởi các Services. Thực hiện sau khi đã lấy được danh sách đối tượng thoả mãn định dạng.
Sẽ thực hiện loại bỏ các field bị trùng theo thứ tự từ trên xuống dưới. Trả về Map<Integer, List<T>>

- *4Consistenck Validator: Được gọi bởi các Controllers. Thực hiện sau khi đã load được đầy đủ danh sách tất cả các
đối tượng, thực hiện đảm bảo nhất đối với danh sách đối tượng phù thuộc vào danh sách các đối tượng khác (vd: Order yêu
cầu các giá trị customerId, productId cần tồn tại)

# Java Fresher 02: Mock 1

## Kiến trúc tổng quan
Dự án sử dụng kiến trúc phân tầng (**Layered Architecture**). Mỗi tầng là một tầng đóng, yêu cầu request đi theo thứ tự từ tầng trên xuống tầng dưới. Dự án bao gồm 3 tầng chính như sau:

### Presentation Layer
- **Chức năng**: Tầng này bao gồm một interface `IBaseController` chính để nhận `function code` và các `controllers` khác thực hiện các chức năng tùy theo `function code` đã nhận.
- **Giao tiếp**: Tầng này chỉ tiếp xúc với `Business Layer` thông qua các `Service`.

### Business Layer
- **Chức năng**: Tầng này có một `Base Service Interface` chứa các phương thức như `loadFile`, `writeFile`, `insert`, `update`, `delete` và các `Service Implement` thực hiện logic riêng cho từng thực thể.
- **Giao tiếp**: Tầng này chỉ tiếp xúc với `Data Access Layer` thông qua các instance của `DataLoader` và `DataWriter`.

### Data Access Layer
- **Chức năng**: Bao gồm các `entity` của dự án và các lớp thao tác với dữ liệu. Trong `datahandler` có hai lớp chính là `DataLoader` và `DataWriter`, được viết dưới dạng `Generic` và gọi các `Functional Interface` riêng cho mỗi `Entity`.
- **Chi tiết**: Các hàm này được định nghĩa trong lớp `DataLineParser` và `DataLineWriter`.

### Infrastructure Layer (Tầng Hạ tầng)
- **Mô tả**: Đây là tầng đặc biệt có thể được truy cập từ tất cả các tầng khác.
- **Chức năng**: Bao gồm các constants, các lớp utility, và các validator.

## Luồng validate file

Quy trình validate file được thực hiện qua 4 giai đoạn:

1. **File Validator**:
    - **Gọi từ**: `FileReaderHelper` và `FileWriterHelper`.
    - **Chức năng**: Được thực hiện ngay sau khi đọc dữ liệu từ file CSV. Loại bỏ các dòng thiếu giá trị của field (giá trị bị `null` hoặc `empty`).
    - **Kết quả**: Trả về `Map<Integer, List<String>>`, trong đó `key` đại diện cho số dòng (row number) và `value` là danh sách các giá trị của dòng đó.

2. **Parser Validator**:
    - **Gọi từ**: `DataLoader`.
    - **Chức năng**: Thực hiện sau khi parse dữ liệu từ danh sách `String` thành danh sách đối tượng. Loại bỏ các dòng có field chứa dữ liệu không đúng kiểu hoặc không đáp ứng yêu cầu (vd: số âm, định dạng sai của `phone`, `email`, `order date`, `product quantities`).
    - **Kết quả**: Trả về `Map<Integer, List<T>>`, trong đó `key` đại diện cho số dòng (row number) và `value` là danh sách các đối tượng hợp lệ của dòng đó.

3. **Unique Validator**:
    - **Gọi từ**: Các `Services`.
    - **Chức năng**: Thực hiện sau khi đã lấy danh sách đối tượng thỏa mãn yêu cầu định dạng, loại bỏ các field bị trùng lặp theo thứ tự từ trên xuống dưới.
    - **Kết quả**: Trả về `Map<Integer, List<T>>`.

4. **Consistency Validator**:
    - **Gọi từ**: Các `Controllers`.
    - **Chức năng**: Thực hiện sau khi đã load đầy đủ danh sách tất cả các đối tượng. Đảm bảo tính nhất quán của danh sách đối tượng phụ thuộc vào các danh sách đối tượng khác (vd: `Order` yêu cầu `customerId` và `productId` phải tồn tại).
