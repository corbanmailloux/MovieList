using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MovieList
{
    class Movie
    {
        private FileInfo file;

        public Movie(FileInfo inFile)
        {
            file = inFile;
        }


        public override string ToString()
        {
            return file.Name;
        }

        public string Name()
        {
            return file.Name;
        }

        public long Size()
        {
            return file.Length;
        }

        public string Path()
        {
            return file.FullName;
        }

        public void OpenInExplorer()
        {
            System.Diagnostics.Process process = new System.Diagnostics.Process();
            System.Diagnostics.ProcessStartInfo startInfo = new System.Diagnostics.ProcessStartInfo();
            startInfo.WindowStyle = System.Diagnostics.ProcessWindowStyle.Hidden;
            startInfo.FileName = "cmd.exe";
            startInfo.Arguments = "/C explorer /select, \"" + file.FullName + "\"";
            process.StartInfo = startInfo;
            process.Start();
        }
    }
}
