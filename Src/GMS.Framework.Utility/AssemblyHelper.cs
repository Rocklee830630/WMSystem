using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Reflection;
using System.Web;

namespace GMS.Framework.Utility
{
    /// <summary>
    /// ���򼯷��丨����
    /// </summary>
    public static class AssemblyHelper
    {
        /// <summary>
        /// �õ���ڳ��򼯣�����Web��Winform
        /// </summary>
        /// <returns></returns>
        public static Assembly GetEntryAssembly()
        {
            var entryAssembly = Assembly.GetEntryAssembly();
            if (entryAssembly != null)
                return entryAssembly;
            
            if (System.Web.HttpContext.Current == null ||
                System.Web.HttpContext.Current.ApplicationInstance == null)
                return Assembly.GetExecutingAssembly();

            var type = System.Web.HttpContext.Current.ApplicationInstance.GetType();
            while (type != null && type.Namespace == "ASP")
            {
                type = type.BaseType;
            }

            return type == null ? null : type.Assembly;
        }
        
        public static IList<Stream> GetResourceStream(Assembly assembly, System.Linq.Expressions.Expression<Func<string, bool>> predicate)
        {
            List<Stream> result = new List<Stream>();

            foreach (string resource in assembly.GetManifestResourceNames())
            {
                if (predicate.Compile().Invoke(resource))
                {
                    result.Add(assembly.GetManifestResourceStream(resource));
                }
            }

            StreamReader sr = new StreamReader(result[0]);
            string r = sr.ReadToEnd();
            result[0].Position = 0;

            return result;
        }

        /// <summary>
        /// ɨ������ҵ��̳���ĳ�������������
        /// </summary>
        /// <param name="inheritType">����</param>
        /// <param name="searchpattern">�ļ�������</param>
        /// <returns></returns>
        public static List<Type> FindTypeByInheritType(Type inheritType, string searchpattern = "*.dll")
        {
            var result = new List<Type>();
            Type attr = inheritType;

            string domain = GetBaseDirectory();
            string[] dllFiles = Directory.GetFiles(domain, searchpattern, SearchOption.TopDirectoryOnly);

            foreach (string dllFileName in dllFiles)
            {
                foreach (Type type in Assembly.LoadFrom(dllFileName).GetLoadableTypes())
                {
                    if (type.BaseType == inheritType)
                    {
                        result.Add(type);
                    }
                }
            }

            return result;
        }

        /// <summary>
        /// ɨ������ҵ�����ĳ��Attribute������PropertyInfo
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="searchpattern">�ļ�������</param>
        /// <returns></returns>
        public static Dictionary<PropertyInfo, T> FindAllPropertyByAttribute<T>(string searchpattern = "*.dll") where T : Attribute
        {
            var result = new Dictionary<PropertyInfo, T>();
            var attr = typeof(T);

            string domain = GetBaseDirectory();
            string[] dllFiles = Directory.GetFiles(domain, searchpattern, SearchOption.TopDirectoryOnly);

            foreach (string dllFileName in dllFiles)
            {
                foreach (Type type in Assembly.LoadFrom(dllFileName).GetLoadableTypes())
                {
                    foreach (var property in type.GetProperties())
                    {
                        var attrs = property.GetCustomAttributes(attr, true);

                        if (attrs.Length == 0)
                            continue;

                        result.Add(property, (T)attrs.First());
                    }
                }
            }


            return result;
        }

        /// <summary>
        /// ɨ������ҵ������ϴ���ĳ��Attribute����������
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="searchpattern">�ļ�������</param>
        /// <returns></returns>
        public static Dictionary<string, List<T>> FindAllTypeByAttribute<T>(string searchpattern = "*.dll") where T : Attribute
        {
            var result = new Dictionary<string, List<T>>();
            Type attr = typeof(T);

            string domain = GetBaseDirectory();
            string[] dllFiles = Directory.GetFiles(domain, searchpattern, SearchOption.TopDirectoryOnly);

            foreach (string dllFileName in dllFiles)
            {
                foreach (Type type in Assembly.LoadFrom(dllFileName).GetLoadableTypes())
                {
                    var typeName = type.AssemblyQualifiedName;

                    var attrs = type.GetCustomAttributes(attr, true);
                    if (attrs.Length == 0)
                        continue;

                    result.Add(typeName, new List<T>());

                    foreach (T a in attrs)
                        result[typeName].Add(a);

                }
            }

            return result;
        }

        /// <summary>
        /// ɨ������ҵ�ʵ����ĳ���ӿڵĵ�һ��ʵ��
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="searchpattern">�ļ�������</param>
        /// <returns></returns>
        public static T FindTypeByInterface<T>(string searchpattern = "*.dll") where T : class
        {     
            var interfaceType = typeof(T);

            string domain = GetBaseDirectory();
            string[] dllFiles = Directory.GetFiles(domain, searchpattern, SearchOption.TopDirectoryOnly);

            foreach (string dllFileName in dllFiles)
            {
                foreach (Type type in Assembly.LoadFrom(dllFileName).GetLoadableTypes())
                {
                    if (interfaceType != type && interfaceType.IsAssignableFrom(type))
                    {
                        var instance = Activator.CreateInstance(type) as T;
                        return instance;
                    }
                }
            }

            return null;
        }

        public static IEnumerable<Type> GetLoadableTypes(this Assembly assembly)
        {
            if (assembly == null) throw new ArgumentNullException("assembly");
            try
            {
                return assembly.GetTypes();
            }
            catch (ReflectionTypeLoadException e)
            {
                return e.Types.Where(t => t != null);
            }
        }

        /// <summary>
        /// �õ���ǰӦ�ó���ĸ�Ŀ¼
        /// </summary>
        /// <returns></returns>
        public static string GetBaseDirectory()
        {
            var baseDirectory = AppDomain.CurrentDomain.SetupInformation.PrivateBinPath;

            if (AppDomain.CurrentDomain.SetupInformation.PrivateBinPath == null)
                baseDirectory = AppDomain.CurrentDomain.BaseDirectory;

            return baseDirectory;
        }

    }
}
