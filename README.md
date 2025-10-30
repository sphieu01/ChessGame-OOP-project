1\. Add thư viện sqlite trong NetBeans:

B1: chuột phải vào tên project → chọn Properties

B2: Chọn tab Libraries

B3: Nhấn nút Add JAR/Folder

B4: Duyệt tới file lib/sqlite-jdbc-3.45.3.0.jar trong folder lib

B5: Nhấn OK để lưu lại



2\. Add thư viện sqlite trong NetBeans:

B1:Chuột phải vào projects  

→ chọn Open Module Settings (hoặc nhấn phím F4)

(dòng cuối cùng trong menu)



B2:Trong cửa sổ vừa mở:

Chọn Modules ở panel bên trái

Chọn tab Dependencies ở bên phải

Nhấn nút + → chọn JARs or Directories…



B3: Duyệt tới file: lib/sqlite-jdbc-3.45.3.0.jar ở folder lib rồi bấm OK

IntelliJ sẽ thêm file này vào danh sách dependencies.

Hãy đảm bảo Scope = Compile (nếu không, chọn lại).

Bấm Apply → OK



NOTE: cảnh báo khi chạy code là do SQLite JDBC (phiên bản mới ≥ 3.45) sử dụng native access (JNI) mà Java 22 trở lên đã chặn mặc định. Lỗi này không ảnh hưởng đến chương trình.

(Tắt cảnh báo = cách thêm lệnh "--enable-native-access=ALL-UNNAMED" vào VM Options)

