



    # from scipy.optimize import fsolve

    # def equation(x):
    # # Định nghĩa phương trình cần giải
    # return ...

    # # Giá trị ban đầu
    # x0 = ...

    # # Giải phương trình
    # result = fsolve(equation, x0)


import pandas as pd
data = [['Alice', 25], ['Bob', 30], ['Carol', 22]]
df = pd.DataFrame(data, columns=['Name', 'Age'])

import pandas as pd

data = [['Alice', 25], ['Bob', 30], ['Carol', 22], ['David', 28], ['Eve', 35]]
columns = ['Name', 'Age']

df = pd.DataFrame(data, columns=columns)

# Tạo tóm tắt thống kê cơ bản
print("\nTóm tắt thống kê cơ bản:")
print(df.describe())


def age_group(age):
    if age < 30:
        return 'Young'
    else:
        return 'Old'

df['Age Group'] = df['Age'].apply(age_group)


df = pd.DataFrame(
    np.random.randint(1, 7, 6000),
    columns = ['one'])
df['two'] = df['one'] + np.random.randint(1, 7, 6000)
ax = df.plot.hist(bins=12, alpha=0.5)

age_groups = df.groupby('Age Group')['Name'].count()
age_percentage = age_groups / age_groups.sum() * 100



